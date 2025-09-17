package com.evroute.service;

import com.evroute.client.OrsClient;
import com.evroute.model.LegSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class DirectionsService {

    private final OrsClient orsClient;
    private final String provider;

    public DirectionsService(OrsClient orsClient, 
                           @Value("${app.directions.provider}") String provider) {
        this.orsClient = orsClient;
        this.provider = provider;
    }

    public Mono<DirectionsResult> getDirections(double startLat, double startLng, 
                                               double endLat, double endLng) {
        if ("ORS".equals(provider)) {
            return orsClient.getDirections(startLat, startLng, endLat, endLng)
                    .map(this::mapOrsResponse);
        } else {
            return Mono.error(new UnsupportedOperationException("Provider " + provider + " not supported"));
        }
    }

    private DirectionsResult mapOrsResponse(OrsClient.DirectionsResponse response) {
        if (response.features == null || response.features.isEmpty()) {
            throw new RuntimeException("No route found");
        }

        OrsClient.DirectionsResponse.Feature feature = response.features.get(0);
        OrsClient.DirectionsResponse.Properties props = feature.properties;
        OrsClient.DirectionsResponse.Geometry geometry = feature.geometry;

        // Convert coordinates from [lng, lat] to [lat, lng] for our model
        List<double[]> polyline = new ArrayList<>();
        for (List<Double> coord : geometry.coordinates) {
            polyline.add(new double[]{coord.get(1), coord.get(0)}); // lat, lng
        }

        double totalDistanceKm = props.summary.distance / 1000.0;
        int totalMinutes = (int) (props.summary.duration / 60.0);

        return new DirectionsResult(
                new LegSummary(polyline, totalDistanceKm, totalMinutes),
                totalDistanceKm,
                totalMinutes
        );
    }

    public static class DirectionsResult {
        private final LegSummary overall;
        private final double totalDistanceKm;
        private final int totalMinutes;

        public DirectionsResult(LegSummary overall, double totalDistanceKm, int totalMinutes) {
            this.overall = overall;
            this.totalDistanceKm = totalDistanceKm;
            this.totalMinutes = totalMinutes;
        }

        // Getters
        public LegSummary getOverall() { return overall; }
        public double getTotalDistanceKm() { return totalDistanceKm; }
        public int getTotalMinutes() { return totalMinutes; }
    }
}

