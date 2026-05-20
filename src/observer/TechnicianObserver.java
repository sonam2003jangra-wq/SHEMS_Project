package observer;
import notification.NotificationService;
// TechnicianObserver class implements the Observer interface
public class TechnicianObserver implements Observer {
    // Stores technician email address
    private String technicianEmail;
    // Handles notification sending operations
    private NotificationService notificationService;
    // Constructor used to initialize technician observer
    public TechnicianObserver(String technicianEmail) {
        // Assigns technician email
        this.technicianEmail = technicianEmail;
        // Creates notification service object
        this.notificationService = new NotificationService();
    }
    // Method triggered when a device malfunction notification occurs
    @Override
    public void update(String message) {
        // Sends malfunction alert email to technician
        notificationService.sendEmail(
                technicianEmail,
                "Device Malfunction Alert",
                message
        );
    }
}