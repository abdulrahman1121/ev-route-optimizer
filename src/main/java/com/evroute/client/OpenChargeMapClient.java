package com.evroute.client;

import com.evroute.model.ChargingStation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class OpenChargeMapClient {

    private final WebClient webClient;

    public OpenChargeMapClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openchargemap.io/v3")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("X-API-Key", "your-api-key-here") // Optional for OCM
                .build();
    }

    @Cacheable("stations")
    public Mono<List<ChargingStation>> findStationsNear(double lat, double lng, double radiusKm) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/poi")
                        .queryParam("latitude", lat)
                        .queryParam("longitude", lng)
                        .queryParam("distance", radiusKm)
                        .queryParam("distanceunit", "KM")
                        .queryParam("maxresults", 100)
                        .queryParam("compact", true)
                        .queryParam("verbose", false)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseStationsResponse);
    }

    @Cacheable("stations")
    public Mono<List<ChargingStation>> findStationsInBbox(double minLat, double minLng, 
                                                          double maxLat, double maxLng) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/poi")
                        .queryParam("boundingbox", String.format("%f,%f,%f,%f", minLat, minLng, maxLat, maxLng))
                        .queryParam("maxresults", 200)
                        .queryParam("compact", true)
                        .queryParam("verbose", false)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseStationsResponse);
    }

    private List<ChargingStation> parseStationsResponse(String response) {
        // This is a simplified parser - in production you'd use proper JSON parsing
        // For MVP, we'll return mock data
        return List.of(
            new ChargingStation("OCM-001", "Shell Recharge Ellensburg", 46.99, -120.55, 
                               List.of("CCS"), 350.0, true),
            new ChargingStation("OCM-002", "Tesla Supercharger - Yakima", 46.60, -120.51, 
                               List.of("Tesla"), 250.0, true),
            new ChargingStation("OCM-003", "Electrify America - Kennewick", 46.21, -119.14, 
                               List.of("CCS", "CHAdeMO"), 150.0, true)
        );
    }

    // Response classes for OCM API (simplified for MVP)
    public static class OcmResponse {
        public List<OcmStation> data;
    }

    public static class OcmStation {
        public String ID;
        public String Title;
        public AddressInfo AddressInfo;
        public List<Connection> Connections;
        public StatusType StatusType;
    }

    public static class AddressInfo {
        public double Latitude;
        public double Longitude;
    }

    public static class Connection {
        public String ConnectionType;
        public double PowerKW;
        public StatusType StatusType;
    }

    public static class StatusType {
        public String Title;
        public boolean IsOperational;
    }
}
