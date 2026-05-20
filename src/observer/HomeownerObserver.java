package observer;
import model.Homeowner;
import notification.NotificationService;
// HomeownerObserver class implements the Observer interface
public class HomeownerObserver implements Observer {
    // Stores homeowner information
    private Homeowner homeowner;
    // Handles notification sending operations
    private NotificationService notificationService;
    // Constructor used to initialize homeowner observer
    public HomeownerObserver(Homeowner homeowner) {
        // Assigns homeowner object
        this.homeowner = homeowner;
        // Creates notification service object
        this.notificationService = new NotificationService();
    }
    // Method triggered when notification updates occur
    @Override
    public void update(String message) {
        // Sends email notification to homeowner
        notificationService.sendEmail(
                homeowner.getEmail(),
                "High Energy Usage Alert",
                message
        );
        // Sends SMS notification to homeowner
        notificationService.sendSMS(
                homeowner.getPhoneNumber(),
                message
        );
    }
}