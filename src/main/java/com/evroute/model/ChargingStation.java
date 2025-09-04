package com.evroute.model;

import java.util.List;

public class ChargingStation {
    private String id;
    private String name;
    private double lat;
    private double lng;
    private List<String> connectors;
    private double maxKw;
    private boolean operational;

    // Default constructor
    public ChargingStation() {}

    // Constructor with all fields
    public ChargingStation(String id, String name, double lat, double lng, 
                          List<String> connectors, double maxKw, boolean operational) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.connectors = connectors;
        this.maxKw = maxKw;
        this.operational = operational;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLng() { return lng; }
    public void setLng(double lng) { this.lng = lng; }

    public List<String> getConnectors() { return connectors; }
    public void setConnectors(List<String> connectors) { this.connectors = connectors; }

    public double getMaxKw() { return maxKw; }
    public void setMaxKw(double maxKw) { this.maxKw = maxKw; }

    public boolean isOperational() { return operational; }
    public void setOperational(boolean operational) { this.operational = operational; }

    @Override
    public String toString() {
        return "ChargingStation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", connectors=" + connectors +
                ", maxKw=" + maxKw +
                ", operational=" + operational +
                '}';
    }
}
