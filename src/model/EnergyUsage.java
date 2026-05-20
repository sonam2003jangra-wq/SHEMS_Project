package model;

import java.time.LocalDateTime;

public class EnergyUsage {

    private int applianceId;
    private double usage;
    private LocalDateTime time;

    public EnergyUsage(int applianceId, double usage) {
        this.applianceId = applianceId;
        this.usage = usage;
        this.time = LocalDateTime.now();
    }

    public double getUsage() {
        return usage;
    }

    public LocalDateTime getTime() {
        return time;
    }
}