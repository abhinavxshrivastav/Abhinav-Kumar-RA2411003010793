/**
 * Owns exactly one reason to change: how an account gets persisted.
 * Swap this for a real JDBC/JPA implementation later and nothing
 * else in the codebase needs to know.
 */
public class AccountRepository {

    public void save(BankAccount account) {
        // Pretend this talks to MySQL. In reality just prints.
        System.out.println("[DB] Saving account " + account.getAccountNumber() + " to MySQL...");
    }
}
