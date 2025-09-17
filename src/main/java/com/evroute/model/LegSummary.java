package com.evroute.model;

import java.util.List;

public class LegSummary {
    private List<double[]> polyline; // lat,lng pairs
    private double distanceKm;
    private int driveMinutes;

    // Default constructor
    public LegSummary() {}

    // Constructor with all fields
    public LegSummary(List<double[]> polyline, double distanceKm, int driveMinutes) {
        this.polyline = polyline;
        this.distanceKm = distanceKm;
        this.driveMinutes = driveMinutes;
    }

    // Getters and Setters
    public List<double[]> getPolyline() { return polyline; }
    public void setPolyline(List<double[]> polyline) { this.polyline = polyline; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public int getDriveMinutes() { return driveMinutes; }
    public void setDriveMinutes(int driveMinutes) { this.driveMinutes = driveMinutes; }

    @Override
    public String toString() {
        return "LegSummary{" +
                "polyline=" + polyline +
                ", distanceKm=" + distanceKm +
                ", driveMinutes=" + driveMinutes +
                '}';
    }
}

