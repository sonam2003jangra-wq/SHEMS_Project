package controller;

import model.*;
import strategy.PricingStrategy;

public class EnergyController {

    private EnergyManagementSystem system;

    public EnergyController(EnergyManagementSystem system) {
        this.system = system;
    }

    public void addAppliance(Appliance appliance) {
        system.addAppliance(appliance);
    }

    public void turnOnDevice(int id) {
        for (Appliance appliance : system.getAppliances()) {
            if (appliance.getId() == id) {
                appliance.turnOn();
            }
        }
    }

    public void turnOffDevice(int id) {
        for (Appliance appliance : system.getAppliances()) {
            if (appliance.getId() == id) {
                appliance.turnOff();
            }
        }
    }

    public void changePricingPlan(PricingStrategy strategy) {
        system.setPricingStrategy(strategy);
    }

    public void checkHighUsage(double threshold) {
        if (system.calculateTotalUsage() > threshold) {
            system.notifyObservers("Energy usage is above the safe limit.");
        }
    }

    public void checkDeviceFaults() {
        for (Appliance appliance : system.getAppliances()) {
            if (appliance.isMalfunction()) {
                system.notifyObservers(appliance.getName() + " has malfunctioned.");
            }
        }
    }
}