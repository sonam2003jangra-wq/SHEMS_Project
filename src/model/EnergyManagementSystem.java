package model;
import observer.Observer;
import strategy.FlatRatePricing;
import strategy.PricingStrategy;
import java.util.ArrayList;
import java.util.List;

// Singleton class responsible for managing the entire smart-home system
public class EnergyManagementSystem {
    // Stores the single instance of EnergyManagementSystem
    private static EnergyManagementSystem instance;
    // List used to store all smart-home appliances
    private List<Appliance> appliances;
    // List used to store registered observers
    private List<Observer> observers;
    // Stores the active electricity pricing strategy
    private PricingStrategy pricingStrategy;
    // Private constructor prevents external object creation
    private EnergyManagementSystem() {
        // Initializes appliance list
        appliances = new ArrayList<>();
        // Initializes observer list
        observers = new ArrayList<>();
        // Sets default pricing strategy
        pricingStrategy = new FlatRatePricing();
    }
    // Returns the single instance of the system
    public static EnergyManagementSystem getInstance() {
        // Creates instance only if it does not already exist
        if (instance == null) {
            instance = new EnergyManagementSystem();
        }
        // Returns Singleton instance
        return instance;
    }

    // Adds a new appliance into the system
    public void addAppliance(Appliance appliance) {
        appliances.add(appliance);
    }

    // Returns all registered appliances
    public List<Appliance> getAppliances() {
        return appliances;
    }

    // Registers a new observer into the system
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    // Sends notifications to all observers
    public void notifyObservers(String message) {

        // Loops through all observers
        for (Observer observer : observers) {

            // Sends update message
            observer.update(message);
        }
    }

    // Changes the current pricing strategy dynamically
    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    // Returns the active pricing plan name
    public String getPricingPlanName() {
        return pricingStrategy.getPlanName();
    }

    // Calculates total appliance energy usage
    public double calculateTotalUsage() {

        // Stores total energy usage
        double total = 0;

        // Loops through all appliances
        for (Appliance appliance : appliances) {

            // Adds current appliance usage
            total += appliance.getCurrentUsage();
        }

        // Returns total energy usage
        return total;
    }

    // Calculates total electricity cost
    public double calculateTotalCost() {

        // Uses current pricing strategy for cost calculation
        return pricingStrategy.calculateCost(calculateTotalUsage());
    }
}