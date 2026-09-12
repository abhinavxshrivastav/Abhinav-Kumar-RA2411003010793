import java.util.List;

/**
 * GreenLeaf Bank — Section 4, Tasks 3 and 4: the persistence abstraction.
 *
 * In Lab 1 this was a concrete class that printed "[DB] Saving...". It is now
 * the interface Bank depends on, so the storage decision — in memory today, a
 * text file this sprint, MySQL or Mongo later — never reaches Bank at all.
 *
 * Owns exactly one reason to change: what the bank needs to ask of storage.
 * NOT how any particular store answers.
 */
public interface AccountRepository {

    void save(BankAccount account);

    List<AccountRecord> findAll();
}
