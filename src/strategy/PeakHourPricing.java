package strategy;
// PeakHourPricing class implements the PricingStrategy interface
public class PeakHourPricing implements PricingStrategy {
    // Calculates electricity cost during peak-hour usage
    @Override
    public double calculateCost(double usage) {
        // Returns total cost using higher peak-hour pricing rate
        return usage * 0.50;
    }
    // Returns the name of the current pricing strategy
    @Override
    public String getPlanName() {
        // Displays pricing strategy name
        return "Peak Hour Pricing";
    }
}