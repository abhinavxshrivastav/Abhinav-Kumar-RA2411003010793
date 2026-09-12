/**
 * Owns exactly one reason to change: how a statement is formatted.
 * Switching from plain text to, say, a PDF or HTML statement only
 * touches this class.
 *
 * Section 4 (ISP/DIP): the parameter is StatementProvider, not BankAccount.
 * This class no longer knows that accounts exist — it formats anything that
 * can answer four questions about itself.
 */
public class StatementGenerator {

    public String generate(StatementProvider account) {
        StringBuilder sb = new StringBuilder();

        sb.append("---- Statement for Account #")
          .append(account.getAccountNumber())
          .append(" (")
          .append(account.getName())
          .append(") ----\n");

        for (String entry : account.getTransactionLog()) {
            sb.append(entry).append("\n");
        }

        sb.append("Current Balance: Rs. ").append(account.getBalance()).append("\n");
        sb.append("-----------------------------------------------------");

        return sb.toString();
    }
}
