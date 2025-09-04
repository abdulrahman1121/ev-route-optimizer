package com.evroute.model;

import java.util.List;

public class RoutePlan {
    private LegSummary overall;
    private List<PlannedStop> stops;
    private List<LegSummary> legs;
    private double totalEnergyKwh;
    private int totalDriveMinutes;
    private int totalChargeMinutes;

    // Default constructor
    public RoutePlan() {}

    // Constructor with all fields
    public RoutePlan(LegSummary overall, List<PlannedStop> stops, List<LegSummary> legs,
                     double totalEnergyKwh, int totalDriveMinutes, int totalChargeMinutes) {
        this.overall = overall;
        this.stops = stops;
        this.legs = legs;
        this.totalEnergyKwh = totalEnergyKwh;
        this.totalDriveMinutes = totalDriveMinutes;
        this.totalChargeMinutes = totalChargeMinutes;
    }

    // Getters and Setters
    public LegSummary getOverall() { return overall; }
    public void setOverall(LegSummary overall) { this.overall = overall; }

    public List<PlannedStop> getStops() { return stops; }
    public void setStops(List<PlannedStop> stops) { this.stops = stops; }

    public List<LegSummary> getLegs() { return legs; }
    public void setLegs(List<LegSummary> legs) { this.legs = legs; }

    public double getTotalEnergyKwh() { return totalEnergyKwh; }
    public void setTotalEnergyKwh(double totalEnergyKwh) { this.totalEnergyKwh = totalEnergyKwh; }

    public int getTotalDriveMinutes() { return totalDriveMinutes; }
    public void setTotalDriveMinutes(int totalDriveMinutes) { this.totalDriveMinutes = totalDriveMinutes; }

    public int getTotalChargeMinutes() { return totalChargeMinutes; }
    public void setTotalChargeMinutes(int totalChargeMinutes) { this.totalChargeMinutes = totalChargeMinutes; }

    // Helper methods
    public int getTotalMinutes() {
        return totalDriveMinutes + totalChargeMinutes;
    }

    public String getFormattedTotalTime() {
        int hours = getTotalMinutes() / 60;
        int minutes = getTotalMinutes() % 60;
        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dm", minutes);
        }
    }

    public String getFormattedDriveTime() {
        int hours = totalDriveMinutes / 60;
        int minutes = totalDriveMinutes % 60;
        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dm", minutes);
        }
    }

    public String getFormattedChargeTime() {
        int hours = totalChargeMinutes / 60;
        int minutes = totalChargeMinutes % 60;
        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes);
        } else {
            return String.format("%dm", minutes);
        }
    }

    @Override
    public String toString() {
        return "RoutePlan{" +
                "overall=" + overall +
                ", stops=" + stops +
                ", legs=" + legs +
                ", totalEnergyKwh=" + totalEnergyKwh +
                ", totalDriveMinutes=" + totalDriveMinutes +
                ", totalChargeMinutes=" + totalChargeMinutes +
                '}';
    }
}
