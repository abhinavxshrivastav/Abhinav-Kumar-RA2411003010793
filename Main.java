/**
 * Wires the split-up pieces together. This is the only class that
 * knows about BankAccount, AccountRepository, NotificationService,
 * and StatementGenerator all at once — everyone else only knows
 * their own one job.
 */
public class Main {

    public static void main(String[] args) {

        AccountRepository accountRepository = new AccountRepository();
        NotificationService notificationService = new NotificationService();
        StatementGenerator statementGenerator = new StatementGenerator();

        BankAccount account = new BankAccount(1001, "Abhinav", 20, 1000.0, "Savings");
        accountRepository.save(account);
        notificationService.send("To: " + account.getName() +
                " | Account opened with balance Rs. " + account.getBalance());

        account.deposit(500.0);
        accountRepository.save(account);
        notificationService.send("To: " + account.getName() +
                " | Your deposit of Rs. 500.0 was successful. New balance: " + account.getBalance());

        account.withdraw(200.0, null);
        accountRepository.save(account);
        notificationService.send("To: " + account.getName() +
                " | Your withdrawal of Rs. 200.0 was successful. New balance: " + account.getBalance());

        System.out.println(statementGenerator.generate(account));
    }
}
