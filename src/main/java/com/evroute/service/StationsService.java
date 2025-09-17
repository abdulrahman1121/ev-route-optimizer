package com.evroute.service;

import com.evroute.client.OpenChargeMapClient;
import com.evroute.model.ChargingStation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StationsService {

    private final OpenChargeMapClient ocmClient;
    private final String provider;

    public StationsService(OpenChargeMapClient ocmClient, 
                          @Value("${app.stations.provider}") String provider) {
        this.ocmClient = ocmClient;
        this.provider = provider;
    }

    public Mono<List<ChargingStation>> findStationsNear(double lat, double lng, double radiusKm) {
        if ("OCM".equals(provider)) {
            return ocmClient.findStationsNear(lat, lng, radiusKm);
        } else {
            return Mono.error(new UnsupportedOperationException("Provider " + provider + " not supported"));
        }
    }

    public Mono<List<ChargingStation>> findStationsInBbox(double minLat, double minLng, 
                                                          double maxLat, double maxLng) {
        if ("OCM".equals(provider)) {
            return ocmClient.findStationsInBbox(minLat, minLng, maxLat, maxLng);
        } else {
            return Mono.error(new UnsupportedOperationException("Provider " + provider + " not supported"));
        }
    }

    public Mono<List<ChargingStation>> findStationsAlongRoute(List<double[]> routePolyline, 
                                                              double searchRadiusKm) {
        if (routePolyline == null || routePolyline.isEmpty()) {
            return Mono.just(List.of());
        }

        // Calculate bounding box around the route
        double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE;
        double minLng = Double.MAX_VALUE, maxLng = Double.MIN_VALUE;

        for (double[] coord : routePolyline) {
            double lat = coord[0];
            double lng = coord[1];
            minLat = Math.min(minLat, lat);
            maxLat = Math.max(maxLat, lat);
            minLng = Math.min(minLng, lng);
            maxLng = Math.max(maxLng, lng);
        }

        // Expand bounding box by search radius
        double latExpansion = searchRadiusKm / 111.0; // Rough conversion: 1 degree lat ≈ 111 km
        double lngExpansion = searchRadiusKm / (111.0 * Math.cos(Math.toRadians((minLat + maxLat) / 2)));

        minLat -= latExpansion;
        maxLat += latExpansion;
        minLng -= lngExpansion;
        maxLng += lngExpansion;

        return findStationsInBbox(minLat, minLng, maxLat, maxLng);
    }
}

