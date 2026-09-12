import java.util.List;

/**
 * GreenLeaf Bank — Section 4, Task 2. "I can supply the raw material for a
 * statement."
 *
 * Four methods, but one cohesive reason to exist: this is the complete set a
 * statement needs, and nothing else. StatementGenerator now depends on this
 * instead of on the concrete BankAccount, so it can format anything that can
 * answer these four questions.
 */
public interface StatementProvider {

    int getAccountNumber();

    String getName();

    double getBalance();

    List<String> getTransactionLog();
}
