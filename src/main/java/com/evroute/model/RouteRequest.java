package com.evroute.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RouteRequest {
    @NotBlank(message = "Origin is required")
    private String origin;
    
    @NotBlank(message = "Destination is required")
    private String destination;
    
    @NotNull(message = "EV specification is required")
    private EVSpec ev;
    
    private RoutePrefs prefs;

    // Default constructor
    public RouteRequest() {}

    // Constructor with required fields
    public RouteRequest(String origin, String destination, EVSpec ev) {
        this.origin = origin;
        this.destination = destination;
        this.ev = ev;
    }

    // Constructor with all fields
    public RouteRequest(String origin, String destination, EVSpec ev, RoutePrefs prefs) {
        this.origin = origin;
        this.destination = destination;
        this.ev = ev;
        this.prefs = prefs;
    }

    // Getters and Setters
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public EVSpec getEv() { return ev; }
    public void setEv(EVSpec ev) { this.ev = ev; }

    public RoutePrefs getPrefs() { return prefs; }
    public void setPrefs(RoutePrefs prefs) { this.prefs = prefs; }

    @Override
    public String toString() {
        return "RouteRequest{" +
                "origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", ev=" + ev +
                ", prefs=" + prefs +
                '}';
    }
}

