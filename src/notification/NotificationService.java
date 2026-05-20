package notification;
// NotificationService class responsible for sending notifications
public class NotificationService {
    // Method used to simulate email notification sending
    public void sendEmail(String email, String subject, String message) {
        // Displays email notification heading
        System.out.println("\nEMAIL NOTIFICATION");
        // Displays recipient email address
        System.out.println("To: " + email);
        // Displays email subject
        System.out.println("Subject: " + subject);
        // Displays notification message
        System.out.println("Message: " + message);
    }
    // Method used to simulate SMS notification sending
    public void sendSMS(String phoneNumber, String message) {
        // Displays SMS notification heading
        System.out.println("\nSMS NOTIFICATION");
        // Displays phone number
        System.out.println("Phone: " + phoneNumber);
        // Displays SMS message
        System.out.println("Message: " + message);
    }
}