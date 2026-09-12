/**
 * Owns exactly one reason to change: how a customer gets notified.
 * Swap this for a real SMTP/SMS provider later and nothing else in
 * the codebase needs to know.
 */
public class NotificationService {

    public void send(String message) {
        // Pretend this talks to an SMTP server. In reality just prints.
        System.out.println("[EMAIL] Sending: " + message);
    }
}
