package strategy;

public class GreenEnergyPricing implements PricingStrategy {

    public double calculateCost(double usage) {
        return usage * 0.20;
    }

    public String getPlanName() {
        return "Green Energy Discount";
    }
}