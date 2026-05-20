package model;

// Light class inherits all properties and behaviours from Appliance class
public class Light extends Appliance {

    // Constructor used to create a Light object
    public Light(int id, String name, double energyUsage) {

        // Calls the parent Appliance constructor
        // to initialise id, name, and energy usage
        super(id, name, energyUsage);
    }
}