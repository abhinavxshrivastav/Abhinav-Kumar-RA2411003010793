import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GreenLeaf Bank — Section 4, Task 4: the manager's file-based store.
 *
 * One line per account: accountNumber,name,balance
 *
 * This class did not exist when Bank.java was written, and Bank.java did not
 * change by one character to accommodate it. The only edit anywhere was the
 * single line in Main that decides which repository to construct.
 *
 * Owns exactly one reason to change: the on-disk format.
 */
public class FileAccountRepository implements AccountRepository {

    private final Path file;
    private final Map<Integer, AccountRecord> rows = new LinkedHashMap<>();

    public FileAccountRepository(String fileName) {
        this.file = Path.of(fileName);
        load();
    }

    private void load() {
        if (!Files.exists(file)) return;
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                if (line.isBlank()) continue;
                AccountRecord record = AccountRecord.fromCsvLine(line);
                rows.put(record.accountNumber(), record);
            }
            System.out.println("[FILE] Loaded " + rows.size() + " account(s) from " + file);
        } catch (IOException e) {
            System.out.println("[FILE] Could not read " + file + ": " + e.getMessage());
        }
    }

    /**
     * Upsert by account number, then rewrite the file. Simple on purpose —
     * a real store would append a journal, but that is a decision this class
     * owns alone, which is the whole point.
     */
    @Override
    public void save(BankAccount account) {

        rows.put(account.getAccountNumber(),
                new AccountRecord(account.getAccountNumber(),
                                  account.getName(),
                                  account.getBalance()));

        List<String> lines = new ArrayList<>();
        for (AccountRecord record : rows.values()) {
            lines.add(record.toCsvLine());
        }

        try {
            Files.write(file, lines, StandardCharsets.UTF_8);
            System.out.println("[FILE] Saved account " + account.getAccountNumber() + " to " + file);
        } catch (IOException e) {
            System.out.println("[FILE] Could not write " + file + ": " + e.getMessage());
        }
    }

    @Override
    public List<AccountRecord> findAll() {
        return new ArrayList<>(rows.values());
    }
}
