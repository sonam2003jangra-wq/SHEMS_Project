package strategy;

// Interface used to define different energy pricing strategies
public interface PricingStrategy {

    // Method used to calculate energy cost
    // based on appliance energy usage
    double calculateCost(double usage);

    // Returns the name of the current pricing plan
    String getPlanName();
}