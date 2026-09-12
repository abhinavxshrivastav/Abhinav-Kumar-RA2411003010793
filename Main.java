/**
 * GreenLeaf Bank — the composition root.
 *
 * This is the one place allowed to know which concrete classes exist. Every
 * other file talks to abstractions (InterestPolicy, NotificationService), so
 * when a new account type or a new channel arrives, this file is the only
 * existing one that changes.
 */
public class Main {

    public static void main(String[] args) {

        AccountRepository accountRepository = new AccountRepository();
        StatementGenerator statementGenerator = new StatementGenerator();

        // Swap this single line for `new SMSNotificationService()` and every
        // message below changes channel. Bank does not change.
        NotificationService notificationService = new EmailNotificationService();

        Bank bank = new Bank(accountRepository, notificationService, statementGenerator);

        // ----------------------------------------------------
        // Task 3 — interest via policy.calculate(balance), no if/else chain
        // ----------------------------------------------------

        BankAccount savings = new BankAccount(1001, "Abhinav", 20, 1000.0, "Savings");
        bank.open(savings);
        bank.deposit(savings, 500.0);
        bank.withdraw(savings, 200.0, null);
        bank.creditInterest(savings, new SavingsInterestPolicy());
        System.out.println(bank.statementFor(savings));

        BankAccount current = new BankAccount(1002, "GreenLeaf Traders", 35, 5000.0, "Current");
        bank.open(current);
        bank.creditInterest(current, new CurrentInterestPolicy());
        System.out.println(bank.statementFor(current));

        // ----------------------------------------------------
        // Task 4 — the new requirement, priced at two brand-new files
        // (SalaryAccount, SalaryInterestPolicy) and these four lines.
        // No existing policy class was opened.
        // ----------------------------------------------------

        SalaryAccount salary = new SalaryAccount(1003, "Meera", 26, 3000.0);
        bank.open(salary);
        bank.creditInterest(salary, new SalaryInterestPolicy());
        System.out.println(bank.statementFor(salary));

        // ----------------------------------------------------
        // Task 4b — same Bank class, different channel. Proof that the
        // notification swap is a constructor argument, not a code change.
        // ----------------------------------------------------

        Bank smsBank = new Bank(accountRepository, new SMSNotificationService(), statementGenerator);
        smsBank.creditInterest(salary, new SalaryInterestPolicy());
    }
}
