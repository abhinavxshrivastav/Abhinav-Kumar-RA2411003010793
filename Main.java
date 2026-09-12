import java.util.ArrayList;
import java.util.List;

/**
 * GreenLeaf Bank — the composition root.
 *
 * This is the one place allowed to know which concrete classes exist. Every
 * other file talks to abstractions (InterestPolicy, NotificationService,
 * Withdrawable), so when a new account type or a new channel arrives, this
 * file is the only existing one that changes.
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
        // Section 2, Task 3 — interest via policy.calculate(balance)
        // ----------------------------------------------------

        SavingsAccount savings = new SavingsAccount(1001, "Abhinav", 20, 1000.0);
        bank.open(savings);
        bank.deposit(savings, 500.0);
        bank.withdraw(savings, 200.0);
        bank.creditInterest(savings, new SavingsInterestPolicy());
        System.out.println(bank.statementFor(savings));

        CurrentAccount current = new CurrentAccount(1002, "GreenLeaf Traders", 35, 5000.0);
        bank.open(current);
        bank.creditInterest(current, new CurrentInterestPolicy());
        System.out.println(bank.statementFor(current));

        // ----------------------------------------------------
        // Section 2, Task 4 — new type, two brand-new files, no existing
        // policy class opened.
        // ----------------------------------------------------

        SalaryAccount salary = new SalaryAccount(1003, "Meera", 26, 3000.0);
        bank.open(salary);
        bank.creditInterest(salary, new SalaryInterestPolicy());
        System.out.println(bank.statementFor(salary));

        // ----------------------------------------------------
        // Section 3, Task 4 — the honest withdrawal run.
        //
        // The list is typed List<Withdrawable>, so it can only ever hold
        // accounts that really can pay out. There is no type check, no
        // try/catch, and no way for a fixed deposit to get in here: the
        // compiler refuses it.
        // ----------------------------------------------------

        FixedDepositAccount fd = new FixedDepositAccount(1004, "Rohan", 41, 100000.0);
        bank.open(fd);
        bank.creditInterest(fd, new FixedDepositInterestPolicy());

        List<Withdrawable> withdrawable = new ArrayList<>();
        withdrawable.add(savings);
        withdrawable.add(current);
        withdrawable.add(salary);
        // withdrawable.add(fd);        // <- will not compile. That is the point.
        // bank.withdraw(fd, 500.0);    // <- will not compile either.

        System.out.println("---- Month-end withdrawal run ----");
        for (Withdrawable account : withdrawable) {
            boolean ok = account.withdraw(250.0);
            System.out.println(account.getClass().getSimpleName()
                    + ": withdrawal of Rs. 250.0 -> " + (ok ? "done" : "declined"));
        }
        System.out.println("Run completed — every element was asked only for "
                + "something it had promised it could do.");
        System.out.println();

        // The fixed deposit is a first-class account throughout: opened,
        // saved, credited with interest and printed. It simply never claims
        // to be withdrawable.
        System.out.println(bank.statementFor(fd));

        // PIN enforcement from Lab 1 is still live on withdrawable types.
        current.setPin(4321);
        System.out.println("PIN-protected withdrawal, wrong PIN -> "
                + current.withdraw(300.0, 1111));
        System.out.println("PIN-protected withdrawal, right PIN -> "
                + current.withdraw(300.0, 4321));
        System.out.println();

        // ----------------------------------------------------
        // Section 2, Task 4b — same Bank class, different channel.
        // ----------------------------------------------------

        Bank smsBank = new Bank(accountRepository, new SMSNotificationService(), statementGenerator);
        smsBank.creditInterest(salary, new SalaryInterestPolicy());
    }
}
