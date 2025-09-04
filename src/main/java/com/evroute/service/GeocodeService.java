package com.evroute.service;

import com.evroute.client.OrsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GeocodeService {

    private final OrsClient orsClient;
    private final String provider;

    public GeocodeService(OrsClient orsClient, 
                         @Value("${app.geocode.provider}") String provider) {
        this.orsClient = orsClient;
        this.provider = provider;
    }

    public Mono<GeocodeResult> geocode(String query) {
        if ("ORS".equals(provider)) {
            return orsClient.geocode(query)
                    .map(this::mapOrsResponse);
        } else {
            return Mono.error(new UnsupportedOperationException("Provider " + provider + " not supported"));
        }
    }

    private GeocodeResult mapOrsResponse(OrsClient.GeocodeResponse response) {
        if (response.features == null || response.features.isEmpty()) {
            throw new RuntimeException("No geocoding results found for query");
        }

        OrsClient.GeocodeResponse.Feature feature = response.features.get(0);
        List<Double> coords = feature.geometry.coordinates;
        
        return new GeocodeResult(
                coords.get(1), // lat
                coords.get(0), // lng
                feature.properties.name,
                feature.properties.state,
                feature.properties.country
        );
    }

    public static class GeocodeResult {
        private final double lat;
        private final double lng;
        private final String name;
        private final String state;
        private final String country;

        public GeocodeResult(double lat, double lng, String name, String state, String country) {
            this.lat = lat;
            this.lng = lng;
            this.name = name;
            this.state = state;
            this.country = country;
        }

        // Getters
        public double getLat() { return lat; }
        public double getLng() { return lng; }
        public String getName() { return name; }
        public String getState() { return state; }
        public String getCountry() { return country; }

        @Override
        public String toString() {
            return String.format("%s, %s, %s (%.6f, %.6f)", name, state, country, lat, lng);
        }
    }
}
