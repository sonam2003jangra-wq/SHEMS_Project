//public class Main {
//
//    public static void main(String[] args) {
//
//        System.out.println("====================================");
//        System.out.println(" SMART HOME ENERGY MANAGEMENT SYSTEM ");
//        System.out.println("====================================");
//
//        System.out.println("\nSystem starting successfully...");
//
//        System.out.println("\nLoading appliances...");
//        System.out.println("Living Room Light loaded.");
//        System.out.println("Bedroom AC loaded.");
//        System.out.println("Kitchen Fridge loaded.");
//
//        System.out.println("\nApplying pricing strategy...");
//        System.out.println("Peak Hour Pricing applied successfully.");
//
//        System.out.println("\nChecking appliance status...");
//        System.out.println("Living Room Light turned ON.");
//        System.out.println("Bedroom AC turned ON.");
//        System.out.println("Kitchen Fridge turned OFF.");
//
//        System.out.println("\nUpdating energy usage graph...");
//        System.out.println("Energy graph updated successfully.");
//
//        System.out.println("\nGenerating notifications...");
//        System.out.println("Notification: High energy usage detected.");
//
//        System.out.println("\nSending Email Notification...");
//        System.out.println("Email sent to homeowner@gmail.com");
//        System.out.println("Subject: High Energy Usage Alert");
//
//        System.out.println("\nSending SMS Notification...");
//        System.out.println("SMS sent to +44XXXXXXXXXX");
//        System.out.println("Message: Appliance usage exceeded threshold.");
//
//        System.out.println("\nRenewable energy notification...");
//        System.out.println("Green energy discount applied successfully.");
//
//        System.out.println("\nSecurity validation...");
//        System.out.println("Password hashing completed using SHA-256.");
//
//        System.out.println("\nRuntime testing completed successfully.");
//        System.out.println("No runtime errors detected.");
//
//        System.out.println("\nSHEMS application is running successfully.");
//    }
//}


import view.SmartHomeApp;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new SmartHomeApp();
        });
    }
}