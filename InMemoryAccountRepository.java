import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GreenLeaf Bank — Lab 1's repository behaviour, now behind the interface.
 *
 * Owns exactly one reason to change: how accounts are held in memory.
 */
public class InMemoryAccountRepository implements AccountRepository {

    private final Map<Integer, AccountRecord> rows = new LinkedHashMap<>();

    @Override
    public void save(BankAccount account) {
        rows.put(account.getAccountNumber(),
                new AccountRecord(account.getAccountNumber(),
                                  account.getName(),
                                  account.getBalance()));
        System.out.println("[MEMORY] Saved account " + account.getAccountNumber());
    }

    @Override
    public List<AccountRecord> findAll() {
        return new ArrayList<>(rows.values());
    }
}
