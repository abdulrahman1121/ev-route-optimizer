package com.evroute.service;

import com.evroute.model.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PlanningService {

    private static final double CHARGE_EFFICIENCY = 0.9;
    private static final double SEARCH_RADIUS_KM = 15.0;
    private static final double DETOUR_PENALTY_FACTOR = 5.0;

    public Mono<RoutePlan> planRoute(RouteRequest request) {
        return Mono.fromCallable(() -> {
            // For MVP, we'll use mock data and implement the planning algorithm
            // In production, this would integrate with the actual services
            
            // Mock route for Seattle to Boise
            List<double[]> mockPolyline = List.of(
                new double[]{47.61, -122.33}, // Seattle
                new double[]{46.99, -120.55}, // Ellensburg
                new double[]{46.60, -120.51}, // Yakima
                new double[]{46.21, -119.14}, // Kennewick
                new double[]{43.62, -116.20}  // Boise
            );

            // Mock charging stops
            List<PlannedStop> stops = List.of(
                new PlannedStop(
                    new ChargingStation("OCM-001", "Shell Recharge Ellensburg", 46.99, -120.55, 
                                       List.of("CCS"), 350.0, true),
                    0.22, 0.70, 18, 32.4
                ),
                new PlannedStop(
                    new ChargingStation("OCM-002", "Tesla Supercharger - Yakima", 46.60, -120.51, 
                                       List.of("Tesla"), 250.0, true),
                    0.15, 0.65, 22, 37.0
                )
            );

            // Mock legs
            List<LegSummary> legs = List.of(
                new LegSummary(List.of(mockPolyline.get(0), mockPolyline.get(1)), 280.5, 162),
                new LegSummary(List.of(mockPolyline.get(1), mockPolyline.get(2)), 310.2, 186),
                new LegSummary(List.of(mockPolyline.get(2), mockPolyline.get(3)), 221.6, 120)
            );

            return new RoutePlan(
                new LegSummary(mockPolyline, 812.3, 468),
                stops,
                legs,
                120.7,
                468,
                40
            );
        });
    }

    public RoutePlan planRouteWithAlgorithm(RouteRequest request, 
                                          DirectionsService.DirectionsResult directions,
                                          List<ChargingStation> stations) {
        
        List<PlannedStop> stops = new ArrayList<>();
        List<LegSummary> legs = new ArrayList<>();
        
        double currentSoC = request.getEv().getStartSoC();
        double usableKwh = request.getEv().getUsableKwh();
        double consumptionWhPerKm = request.getEv().getConsumptionWhPerKm();
        
        // Calculate total energy needed
        double totalDistanceKm = directions.getTotalDistanceKm();
        double totalEnergyKwh = (totalDistanceKm * consumptionWhPerKm) / 1000.0;
        
        // Check if we can make it without charging
        double maxRangeKm = request.getEv().getPlanningRangeKm(currentSoC);
        if (totalDistanceKm <= maxRangeKm) {
            // No charging needed
            return new RoutePlan(
                directions.getOverall(),
                List.of(),
                List.of(directions.getOverall()),
                totalEnergyKwh,
                directions.getTotalMinutes(),
                0
            );
        }

        // Find charging stops along the route
        List<double[]> routePolyline = directions.getOverall().getPolyline();
        List<ChargingStation> routeStations = findStationsAlongRoute(routePolyline, stations, SEARCH_RADIUS_KM);
        
        // Simple greedy algorithm: find farthest reachable station
        double currentKm = 0;
        double remainingDistance = totalDistanceKm;
        
        while (remainingDistance > maxRangeKm) {
            // Find candidates within range
            List<ChargingStation> candidates = findStationsInRange(routeStations, currentKm, maxRangeKm);
            
            if (candidates.isEmpty()) {
                // No stations in range - this route is not possible
                throw new RuntimeException("No charging stations available within range. Consider increasing start SOC or choosing a different route.");
            }
            
            // Pick the station that maximizes progress along the route
            ChargingStation bestStation = findBestStation(candidates, currentKm, routePolyline);
            
            // Calculate arrival SOC
            double distanceToStation = calculateDistanceAlongRoute(currentKm, bestStation, routePolyline);
            double energyToStation = (distanceToStation * consumptionWhPerKm) / 1000.0;
            double arriveSoC = currentSoC - (energyToStation / usableKwh);
            
            // Calculate departure SOC needed
            double nextLegDistance = Math.min(remainingDistance - distanceToStation, maxRangeKm * 0.8);
            double energyNeeded = (nextLegDistance * consumptionWhPerKm) / 1000.0;
            double departSoC = Math.min(1.0, arriveSoC + (energyNeeded / usableKwh));
            
            // Calculate charge time
            double energyAddedKwh = (departSoC - arriveSoC) * usableKwh;
            double avgKw = Math.min(bestStation.getMaxKw(), request.getEv().getMaxChargeKw()) * CHARGE_EFFICIENCY;
            int chargeMinutes = (int) ((energyAddedKwh / avgKw) * 60);
            
            // Create planned stop
            PlannedStop stop = new PlannedStop(bestStation, arriveSoC, departSoC, chargeMinutes, energyAddedKwh);
            stops.add(stop);
            
            // Update state
            currentSoC = departSoC;
            currentKm += distanceToStation;
            remainingDistance -= distanceToStation;
            
            // Create leg summary
            legs.add(new LegSummary(
                extractRouteSegment(routePolyline, currentKm - distanceToStation, currentKm),
                distanceToStation,
                (int) (distanceToStation / 100.0 * 60) // Rough estimate: 100 km/h
            ));
        }
        
        // Final leg to destination
        legs.add(new LegSummary(
            extractRouteSegment(routePolyline, currentKm, totalDistanceKm),
            remainingDistance,
            (int) (remainingDistance / 100.0 * 60)
        ));
        
        return new RoutePlan(
            directions.getOverall(),
            stops,
            legs,
            totalEnergyKwh,
            directions.getTotalMinutes(),
            stops.stream().mapToInt(PlannedStop::getChargeMinutes).sum()
        );
    }

    private List<ChargingStation> findStationsAlongRoute(List<double[]> routePolyline, 
                                                        List<ChargingStation> allStations, 
                                                        double radiusKm) {
        List<ChargingStation> routeStations = new ArrayList<>();
        
        for (ChargingStation station : allStations) {
            if (isStationNearRoute(station, routePolyline, radiusKm)) {
                routeStations.add(station);
            }
        }
        
        return routeStations;
    }

    private boolean isStationNearRoute(ChargingStation station, List<double[]> routePolyline, double radiusKm) {
        for (double[] coord : routePolyline) {
            double distance = calculateDistance(station.getLat(), station.getLng(), coord[0], coord[1]);
            if (distance <= radiusKm) {
                return true;
            }
        }
        return false;
    }

    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        // Haversine formula for great circle distance
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return 6371 * c; // Earth radius in km
    }

    private List<ChargingStation> findStationsInRange(List<ChargingStation> stations, 
                                                     double currentKm, double rangeKm) {
        // This is a simplified version - in production you'd calculate actual distances along the route
        return stations.stream()
                .filter(station -> true) // Simplified for MVP
                .toList();
    }

    private ChargingStation findBestStation(List<ChargingStation> candidates, 
                                          double currentKm, List<double[]> routePolyline) {
        // For MVP, just return the first candidate
        // In production, you'd score by progress along route vs detour
        return candidates.get(0);
    }

    private double calculateDistanceAlongRoute(double currentKm, ChargingStation station, 
                                            List<double[]> routePolyline) {
        // Simplified for MVP - in production you'd calculate actual route distance
        return 50.0; // Mock 50km
    }

    private List<double[]> extractRouteSegment(List<double[]> polyline, double startKm, double endKm) {
        // Simplified for MVP - in production you'd extract the actual segment
        return polyline;
    }
}
