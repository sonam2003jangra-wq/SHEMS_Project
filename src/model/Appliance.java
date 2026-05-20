package model;
// Abstract parent class representing all smart-home appliances
public abstract class Appliance {
    // Unique appliance ID
    protected int id;
    // Name of the appliance
    protected String name;
    // Energy consumption value per usage cycle/hour
    protected double energyUsage;
    // Stores appliance ON/OFF status
    protected boolean isOn;
    // Indicates whether the appliance has malfunctioned
    protected boolean malfunction;
    // Constructor to initialize appliance details
    public Appliance(int id, String name, double energyUsage) {
        this.id = id;                     // Assign appliance ID
        this.name = name;                 // Assign appliance name
        this.energyUsage = energyUsage;   // Assign energy usage value
        this.isOn = false;                // Appliance initially OFF
        this.malfunction = false;         // No malfunction initially
    }
    // Method to switch ON the appliance
    public void turnOn() {
        isOn = true;}
    // Method to switch OFF the appliance
    public void turnOff() {
        isOn = false;}
    // Returns current energy usage only if appliance is ON
    public double getCurrentUsage() {
        return isOn ? energyUsage : 0;
    }
    // Getter method for appliance ID
    public int getId() {
        return id;
    }
    // Getter method for appliance name
    public String getName() {
        return name;
    }
    // Checks whether appliance is malfunctioning
    public boolean isMalfunction() {
        return malfunction;
    }
    // Updates malfunction status of appliance
    public void setMalfunction(boolean malfunction) {
        this.malfunction = malfunction;
    }
}