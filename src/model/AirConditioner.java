package model;

// AirConditioner class inherits from the Appliance parent class
public class AirConditioner extends Appliance {

    // Constructor used to create an AirConditioner object
    public AirConditioner(int id, String name, double energyUsage) {

        // Calls the Appliance constructor
        // to initialise appliance ID, name, and energy usage
        super(id, name, energyUsage);
    }
}