package com.evroute.client;

import com.evroute.model.LegSummary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class OrsClient {

    private final WebClient webClient;
    private final String apiKey;

    public OrsClient(@Value("${ors.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openrouteservice.org")
                .defaultHeader("Authorization", apiKey)
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Cacheable("geocode")
    public Mono<GeocodeResponse> geocode(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/geocode/search")
                        .queryParam("text", query)
                        .queryParam("size", "1")
                        .build())
                .retrieve()
                .bodyToMono(GeocodeResponse.class);
    }

    @Cacheable("directions")
    public Mono<DirectionsResponse> getDirections(double startLat, double startLng, 
                                                double endLat, double endLng) {
        String coordinates = String.format("%f,%f|%f,%f", startLng, startLat, endLng, endLat);
        
        return webClient.post()
                .uri("/v2/directions/driving-car")
                .bodyValue(Map.of(
                        "coordinates", coordinates,
                        "instructions", false,
                        "geometry", "geojson",
                        "elevation", false
                ))
                .retrieve()
                .bodyToMono(DirectionsResponse.class);
    }

    // Response classes for ORS API
    public static class GeocodeResponse {
        public List<Feature> features;
        
        public static class Feature {
            public Geometry geometry;
            public Properties properties;
        }
        
        public static class Geometry {
            public List<Double> coordinates; // [lng, lat]
        }
        
        public static class Properties {
            public String name;
            public String country;
            public String state;
        }
    }

    public static class DirectionsResponse {
        public List<Feature> features;
        
        public static class Feature {
            public Properties properties;
            public Geometry geometry;
        }
        
        public static class Properties {
            public Summary summary;
            public List<Segment> segments;
        }
        
        public static class Summary {
            public double distance; // meters
            public double duration; // seconds
        }
        
        public static class Segment {
            public double distance; // meters
            public double duration; // seconds
            public List<Step> steps;
        }
        
        public static class Step {
            public double distance; // meters
            public double duration; // seconds
            public List<Double> wayPoints; // [lng, lat, lng, lat, ...]
        }
        
        public static class Geometry {
            public List<List<Double>> coordinates; // [[lng, lat], [lng, lat], ...]
        }
    }
}

