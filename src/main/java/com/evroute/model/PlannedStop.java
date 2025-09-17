package com.evroute.model;

public class PlannedStop {
    private ChargingStation station;
    private double arriveSoC;
    private double departSoC;
    private int chargeMinutes;
    private double energyAddedKwh;

    // Default constructor
    public PlannedStop() {}

    // Constructor with all fields
    public PlannedStop(ChargingStation station, double arriveSoC, double departSoC, 
                      int chargeMinutes, double energyAddedKwh) {
        this.station = station;
        this.arriveSoC = arriveSoC;
        this.departSoC = departSoC;
        this.chargeMinutes = chargeMinutes;
        this.energyAddedKwh = energyAddedKwh;
    }

    // Getters and Setters
    public ChargingStation getStation() { return station; }
    public void setStation(ChargingStation station) { this.station = station; }

    public double getArriveSoC() { return arriveSoC; }
    public void setArriveSoC(double arriveSoC) { this.arriveSoC = arriveSoC; }

    public double getDepartSoC() { return departSoC; }
    public void setDepartSoC(double departSoC) { this.departSoC = departSoC; }

    public int getChargeMinutes() { return chargeMinutes; }
    public void setChargeMinutes(int chargeMinutes) { this.chargeMinutes = chargeMinutes; }

    public double getEnergyAddedKwh() { return energyAddedKwh; }
    public void setEnergyAddedKwh(double energyAddedKwh) { this.energyAddedKwh = energyAddedKwh; }

    @Override
    public String toString() {
        return "PlannedStop{" +
                "station=" + station +
                ", arriveSoC=" + arriveSoC +
                ", departSoC=" + departSoC +
                ", chargeMinutes=" + chargeMinutes +
                ", energyAddedKwh=" + energyAddedKwh +
                '}';
    }
}

