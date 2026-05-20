package strategy;
// FlatRatePricing class implements the PricingStrategy interface
public class FlatRatePricing implements PricingStrategy {
    // Calculates electricity cost using a fixed flat-rate price
    @Override
    public double calculateCost(double usage) {

        // Returns total cost based on energy usage
        return usage * 0.30;
    }
    // Returns the name of the current pricing plan
    @Override
    public String getPlanName() {

        // Displays pricing strategy name
        return "Flat Rate Pricing";
    }
}