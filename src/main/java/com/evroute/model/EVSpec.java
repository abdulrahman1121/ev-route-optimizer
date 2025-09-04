package com.evroute.model;

public class EVSpec {
    private double batteryKwh;
    private double usableSoCFraction;
    private double consumptionWhPerKm;
    private double maxChargeKw;
    private double startSoC;
    private double reserveSoC;

    // Default constructor
    public EVSpec() {}

    // Constructor with all fields
    public EVSpec(double batteryKwh, double usableSoCFraction, double consumptionWhPerKm, 
                  double maxChargeKw, double startSoC, double reserveSoC) {
        this.batteryKwh = batteryKwh;
        this.usableSoCFraction = usableSoCFraction;
        this.consumptionWhPerKm = consumptionWhPerKm;
        this.maxChargeKw = maxChargeKw;
        this.startSoC = startSoC;
        this.reserveSoC = reserveSoC;
    }

    // Getters and Setters
    public double getBatteryKwh() { return batteryKwh; }
    public void setBatteryKwh(double batteryKwh) { this.batteryKwh = batteryKwh; }

    public double getUsableSoCFraction() { return usableSoCFraction; }
    public void setUsableSoCFraction(double usableSoCFraction) { this.usableSoCFraction = usableSoCFraction; }

    public double getConsumptionWhPerKm() { return consumptionWhPerKm; }
    public void setConsumptionWhPerKm(double consumptionWhPerKm) { this.consumptionWhPerKm = consumptionWhPerKm; }

    public double getMaxChargeKw() { return maxChargeKw; }
    public void setMaxChargeKw(double maxChargeKw) { this.maxChargeKw = maxChargeKw; }

    public double getStartSoC() { return startSoC; }
    public void setStartSoC(double startSoC) { this.startSoC = startSoC; }

    public double getReserveSoC() { return reserveSoC; }
    public void setReserveSoC(double reserveSoC) { this.reserveSoC = reserveSoC; }

    // Helper methods
    public double getUsableKwh() {
        return batteryKwh * usableSoCFraction;
    }

    public double getUsableKmPerFull() {
        return (getUsableKwh() * 1000) / consumptionWhPerKm;
    }

    public double getPlanningRangeKm(double currentSoC) {
        return getUsableKmPerFull() * (currentSoC - reserveSoC);
    }

    @Override
    public String toString() {
        return "EVSpec{" +
                "batteryKwh=" + batteryKwh +
                ", usableSoCFraction=" + usableSoCFraction +
                ", consumptionWhPerKm=" + consumptionWhPerKm +
                ", maxChargeKw=" + maxChargeKw +
                ", startSoC=" + startSoC +
                ", reserveSoC=" + reserveSoC +
                '}';
    }
}
