package security;
import java.security.MessageDigest;
public class SecurityUtil {
    // Method used to hash user passwords using SHA-256
    public static String hashPassword(String password) {
        try {
            // Create SHA-256 message digest object
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Convert password into hashed byte array
            byte[] hashBytes = md.digest(password.getBytes());
            // StringBuilder used to store hexadecimal hash value
            StringBuilder sb = new StringBuilder();
            // Convert each byte into hexadecimal format
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            // Return final hashed password
            return sb.toString();
        }catch (Exception e) {
            // Throw runtime exception if hashing process fails
            throw new RuntimeException("Password hashing failed.");
        }
    }
}