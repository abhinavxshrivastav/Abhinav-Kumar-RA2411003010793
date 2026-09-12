/**
 * GreenLeaf Bank — Section 2, Task 4.
 *
 * Bank is wired entirely through abstractions handed to its constructor. It
 * never names EmailNotificationService, SMSNotificationService, or any one
 * InterestPolicy, so a new channel or a new interest rule is a new class plus
 * a different argument at the call site — this file stays shut.
 */
public class Bank {

    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final StatementGenerator statementGenerator;

    public Bank(AccountRepository accountRepository,
                NotificationService notificationService,
                StatementGenerator statementGenerator) {
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
        this.statementGenerator = statementGenerator;
    }

    public void open(BankAccount account) {
        accountRepository.save(account);
        notificationService.send("To: " + account.getName()
                + " | Account opened with balance Rs. " + account.getBalance());
    }

    public void deposit(BankAccount account, double amount) {
        if (!account.deposit(amount)) return;
        accountRepository.save(account);
        notificationService.send("To: " + account.getName()
                + " | Your deposit of Rs. " + amount
                + " was successful. New balance: " + account.getBalance());
    }

    public void withdraw(BankAccount account, double amount, Integer enteredPin) {
        if (!account.withdraw(amount, enteredPin)) return;
        accountRepository.save(account);
        notificationService.send("To: " + account.getName()
                + " | Your withdrawal of Rs. " + amount
                + " was successful. New balance: " + account.getBalance());
    }

    /**
     * The OCP payoff. This method depends on InterestPolicy, not on a list of
     * account types — SalaryInterestPolicy arrived after it was written and
     * it did not change by one character.
     */
    public double creditInterest(BankAccount account, InterestPolicy policy) {
        double interest = policy.calculate(account.getBalance());
        account.deposit(interest);
        accountRepository.save(account);
        notificationService.send("To: " + account.getName()
                + " | Interest of Rs. " + interest
                + " credited. New balance: " + account.getBalance());
        return interest;
    }

    public String statementFor(BankAccount account) {
        return statementGenerator.generate(account);
    }
}
