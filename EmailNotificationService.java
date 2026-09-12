/**
 * GreenLeaf Bank — the email channel. This is Lab 1's NotificationService
 * body, unchanged; only its name and the `implements` clause are new.
 *
 * Owns exactly one reason to change: how an email gets sent.
 */
public class EmailNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        // Pretend this talks to an SMTP server. In reality just prints.
        System.out.println("[EMAIL] Sending: " + message);
    }
}
