package com.evroute.controller;

import com.evroute.model.*;
import com.evroute.service.PlanningService;
import com.evroute.service.StationsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "${cors.allowedOrigins}")
public class RouteController {

    private final PlanningService planningService;
    private final StationsService stationsService;

    public RouteController(PlanningService planningService, StationsService stationsService) {
        this.planningService = planningService;
        this.stationsService = stationsService;
    }

    @PostMapping("/route/plan")
    public ResponseEntity<RoutePlan> planRoute(@Valid @RequestBody RouteRequest request) {
        try {
            RoutePlan plan = planningService.planRoute(request).block();
            return ResponseEntity.ok(plan);
        } catch (Exception e) {
            // In production, you'd have proper error handling
            throw new RuntimeException("Failed to plan route: " + e.getMessage(), e);
        }
    }

    @GetMapping("/stations/near")
    public ResponseEntity<List<ChargingStation>> findStationsNear(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double radiusKm) {
        
        try {
            List<ChargingStation> stations = stationsService.findStationsNear(lat, lng, radiusKm).block();
            return ResponseEntity.ok(stations);
        } catch (Exception e) {
            throw new RuntimeException("Failed to find stations: " + e.getMessage(), e);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(new HealthResponse("OK", "EV Route Optimizer is running"));
    }

    @GetMapping("/ev/presets")
    public ResponseEntity<List<EVPreset>> getEVPresets() {
        List<EVPreset> presets = List.of(
            new EVPreset("Tesla Model 3 Long Range", 75.0, 0.9, 160.0, 250.0, 0.8, 0.1),
            new EVPreset("Tesla Model Y Long Range", 75.0, 0.9, 170.0, 250.0, 0.8, 0.1),
            new EVPreset("Tesla Model S Long Range", 100.0, 0.9, 180.0, 250.0, 0.8, 0.1),
            new EVPreset("Tesla Model X Long Range", 100.0, 0.9, 200.0, 250.0, 0.8, 0.1),
            new EVPreset("Ford Mustang Mach-E", 88.0, 0.9, 175.0, 150.0, 0.8, 0.1),
            new EVPreset("Chevrolet Bolt EV", 66.0, 0.9, 150.0, 55.0, 0.8, 0.1),
            new EVPreset("Nissan Leaf Plus", 62.0, 0.9, 160.0, 100.0, 0.8, 0.1)
        );
        return ResponseEntity.ok(presets);
    }

    public static class HealthResponse {
        private final String status;
        private final String message;

        public HealthResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() { return status; }
        public String getMessage() { return message; }
    }

    public static class EVPreset {
        private final String name;
        private final double batteryKwh;
        private final double usableSoCFraction;
        private final double consumptionWhPerKm;
        private final double maxChargeKw;
        private final double startSoC;
        private final double reserveSoC;

        public EVPreset(String name, double batteryKwh, double usableSoCFraction, 
                       double consumptionWhPerKm, double maxChargeKw, double startSoC, double reserveSoC) {
            this.name = name;
            this.batteryKwh = batteryKwh;
            this.usableSoCFraction = usableSoCFraction;
            this.consumptionWhPerKm = consumptionWhPerKm;
            this.maxChargeKw = maxChargeKw;
            this.startSoC = startSoC;
            this.reserveSoC = reserveSoC;
        }

        // Getters
        public String getName() { return name; }
        public double getBatteryKwh() { return batteryKwh; }
        public double getUsableSoCFraction() { return usableSoCFraction; }
        public double getConsumptionWhPerKm() { return consumptionWhPerKm; }
        public double getMaxChargeKw() { return maxChargeKw; }
        public double getStartSoC() { return startSoC; }
        public double getReserveSoC() { return reserveSoC; }
    }
}
