package test;

import factory.ApplianceFactory;
import model.*;
import security.SecurityUtil;
import strategy.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SHEMSTest {

    // Test SHA-256 password hashing functionality
    @Test
    public void testPasswordHashing() {

        // Original password
        String password = "12345";

        // Generate hashed password
        String hash = SecurityUtil.hashPassword(password);

        // Ensure plain password and hash are different
        assertNotEquals(password, hash);

        // SHA-256 hash should contain 64 characters
        assertEquals(64, hash.length());
    }

    // Test Factory pattern for creating Light appliance
    @Test
    public void testFactoryCreatesLight() {

        // Create Light appliance object
        Appliance light = ApplianceFactory.createAppliance(
                "light", 101, "Living Room Light", 2.5
        );

        // Verify object creation
        assertNotNull(light);

        // Verify correct appliance type
        assertTrue(light instanceof Light);

        // Verify appliance name
        assertEquals("Living Room Light", light.getName());
    }

    // Test Factory pattern for creating AirConditioner appliance
    @Test
    public void testFactoryCreatesAC() {

        // Create AirConditioner appliance object
        Appliance ac = ApplianceFactory.createAppliance(
                "ac", 102, "Bedroom AC", 12.0
        );

        // Verify object creation
        assertNotNull(ac);

        // Verify correct appliance type
        assertTrue(ac instanceof AirConditioner);
    }

    // Test appliance ON/OFF functionality
    @Test
    public void testApplianceTurnOnAndOff() {
        // Create Light appliance
        Appliance light = ApplianceFactory.createAppliance(
                "light", 101, "Living Room Light", 2.5);
        // Initial usage should be zero
        assertEquals(0.0, light.getCurrentUsage());
        // Turn appliance ON
        light.turnOn();
        // Usage should update after turning ON
        assertEquals(2.5, light.getCurrentUsage());
        // Turn appliance OFF
        light.turnOff();
        // Usage should return to zero
        assertEquals(0.0, light.getCurrentUsage());}
    // Test Flat Rate pricing strategy
    @Test
    public void testFlatRatePricing() {
        // Create FlatRatePricing strategy
        PricingStrategy pricing = new FlatRatePricing();
        // Verify cost calculation
        assertEquals(3.0, pricing.calculateCost(10));
        // Verify pricing plan name
        assertEquals("Flat Rate Pricing", pricing.getPlanName());}
    // Test Peak Hour pricing strategy
    @Test
    public void testPeakHourPricing() {
        // Create PeakHourPricing strategy
        PricingStrategy pricing = new PeakHourPricing();
        // Verify peak pricing calculation
        assertEquals(5.0, pricing.calculateCost(10));
        // Verify pricing plan name
        assertEquals("Peak Hour Pricing", pricing.getPlanName());}

    // Test Green Energy pricing strategy
    @Test
    public void testGreenEnergyPricing() {

        // Create GreenEnergyPricing strategy
        PricingStrategy pricing = new GreenEnergyPricing();

        // Verify discounted green-energy cost
        assertEquals(2.0, pricing.calculateCost(10));

        // Verify pricing plan name
        assertEquals("Green Energy Discount", pricing.getPlanName());
    }

    // Test Singleton pattern implementation
    @Test
    public void testSingletonSystem() {

        // Get first instance of system
        EnergyManagementSystem system1 =
                EnergyManagementSystem.getInstance();

        // Get second instance of system
        EnergyManagementSystem system2 =
                EnergyManagementSystem.getInstance();

        // Verify both references point to same object
        assertSame(system1, system2);
    }

    // Test invalid appliance creation handling
    @Test
    public void testInvalidApplianceType() {

        // Verify exception is thrown for invalid appliance type
        assertThrows(IllegalArgumentException.class, () -> {

            ApplianceFactory.createAppliance(
                    "washingmachine",
                    200,
                    "Washing Machine",
                    5.0
            );
        });
    }

    // Test appliance malfunction detection
    @Test
    public void testDeviceMalfunction() {

        // Create AirConditioner appliance
        Appliance ac = ApplianceFactory.createAppliance(
                "ac", 102, "Bedroom AC", 12.0
        );

        // Simulate device malfunction
        ac.setMalfunction(true);

        // Verify malfunction state
        assertTrue(ac.isMalfunction());
    }
}