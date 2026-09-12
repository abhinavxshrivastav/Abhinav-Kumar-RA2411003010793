/**
 * GreenLeaf Bank — Section 2, Task 4: the second channel.
 *
 * Brand-new file. Bank, EmailNotificationService and every policy class
 * stayed shut while this was added — Main just passes this to the Bank
 * constructor instead.
 */
public class SMSNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        // Pretend this talks to an SMS gateway. In reality just prints.
        System.out.println("[SMS] Sending: " + message);
    }
}
