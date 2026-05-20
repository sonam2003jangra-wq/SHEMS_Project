package observer;

// Observer interface used for notification updates
public interface Observer {

    // Method called when a notification message is sent
    void update(String message);
}