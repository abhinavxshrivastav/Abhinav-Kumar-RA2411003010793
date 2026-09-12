/**
 * GreenLeaf Bank — the notification channel, as an abstraction.
 *
 * In Lab 1 this was a concrete email-sending class. Section 2 turns it into
 * the interface Bank depends on: Bank receives one of these through its
 * constructor, so adding SMS — or push, or WhatsApp — means writing a new
 * implementation and changing one argument at the call site. Bank's
 * internals are never reopened.
 */
public interface NotificationService {

    void send(String message);
}
