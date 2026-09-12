/**
 * GreenLeaf Bank — a stored account, as storage sees it.
 *
 * Deliberately not a BankAccount: reading a row back gives you three fields,
 * not a live account with a transaction log and a PIN. Keeping them separate
 * stops the repository from having to guess which subclass to rebuild.
 */
public record AccountRecord(int accountNumber, String name, double balance) {

    public String toCsvLine() {
        return accountNumber + "," + name + "," + balance;
    }

    public static AccountRecord fromCsvLine(String line) {
        String[] parts = line.split(",", 3);
        return new AccountRecord(
                Integer.parseInt(parts[0].trim()),
                parts[1].trim(),
                Double.parseDouble(parts[2].trim()));
    }
}
