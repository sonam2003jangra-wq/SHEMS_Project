package controller;

import model.*;
import strategy.PricingStrategy;

public class EnergyController {

    // Reference to the central Energy Management System (Singleton)
    private EnergyManagementSystem system;

    // Constructor used to initialise the controller with the system instance
    public EnergyController(EnergyManagementSystem system) {
        this.system = system;
    }

    // Adds a new appliance to the system
    public void addAppliance(Appliance appliance) {
        system.addAppliance(appliance);
    }

    // Turns ON a specific appliance using its unique ID
    public void turnOnDevice(int id) {
        for (Appliance appliance : system.getAppliances()) {
            if (appliance.getId() == id) {
                appliance.turnOn();
            }
        }
    }

    // Turns OFF a specific appliance using its unique ID
    public void turnOffDevice(int id) {
        for (Appliance appliance : system.getAppliances()) {
            if (appliance.getId() == id) {
                appliance.turnOff();
            }
        }
    }

    // Changes the active pricing strategy at runtime
    // Implements the Strategy Design Pattern
    public void changePricingPlan(PricingStrategy strategy) {
        system.setPricingStrategy(strategy);
    }

    // Checks whether total energy usage exceeds a specified threshold
    // If exceeded, a notification is sent to registered observers
    public void checkHighUsage(double threshold) {
        if (system.calculateTotalUsage() > threshold) {
            system.notifyObservers("Energy usage is above the safe limit.");
        }
    }

    // Scans all appliances for faults or malfunctions
    // Fault notifications are automatically sent through the Observer Pattern
    public void checkDeviceFaults() {
        for (Appliance appliance : system.getAppliances()) {
            if (appliance.isMalfunction()) {
                system.notifyObservers(
                        appliance.getName() + " has malfunctioned."
                );
            }
        }
    }
}