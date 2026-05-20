package factory;
import model.*;
// Factory class used to create different appliance objects dynamically
public class ApplianceFactory {
    // Static factory method used to generate appliance objects
    public static Appliance createAppliance(String type, int id, String name, double usage) {
        // Checks appliance type entered by the user
        switch (type.toLowerCase()) {
            // Creates and returns Light object
            case "light":
                return new Light(id, name, usage);
            // Creates and returns AirConditioner object
            case "ac":
                return new AirConditioner(id, name, usage);
            // Creates and returns Fridge object
            case "fridge":
                return new Fridge(id, name, usage);
            // Handles invalid appliance types
            default:
                throw new IllegalArgumentException("Invalid appliance type.");
        }
    }
}