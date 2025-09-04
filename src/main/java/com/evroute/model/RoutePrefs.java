package com.evroute.model;

public class RoutePrefs {
    private double targetArrivalSoC = 0.15; // default 15%
    private double planningSpeedKph = 100.0; // default 100 km/h

    // Default constructor
    public RoutePrefs() {}

    // Constructor with all fields
    public RoutePrefs(double targetArrivalSoC, double planningSpeedKph) {
        this.targetArrivalSoC = targetArrivalSoC;
        this.planningSpeedKph = planningSpeedKph;
    }

    // Getters and Setters
    public double getTargetArrivalSoC() { return targetArrivalSoC; }
    public void setTargetArrivalSoC(double targetArrivalSoC) { this.targetArrivalSoC = targetArrivalSoC; }

    public double getPlanningSpeedKph() { return planningSpeedKph; }
    public void setPlanningSpeedKph(double planningSpeedKph) { this.planningSpeedKph = planningSpeedKph; }

    @Override
    public String toString() {
        return "RoutePrefs{" +
                "targetArrivalSoC=" + targetArrivalSoC +
                ", planningSpeedKph=" + planningSpeedKph +
                '}';
    }
}
