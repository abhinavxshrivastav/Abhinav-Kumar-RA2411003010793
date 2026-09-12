import java.util.ArrayList;
import java.util.List;

/**
 * GreenLeaf Bank — the composition root.
 *
 * This is the one place allowed to know which concrete classes exist. Every
 * other file talks to abstractions (InterestPolicy, NotificationService,
 * AccountRepository, and the capability interfaces), so when a new account
 * type, channel or storage backend arrives, this file is the only existing
 * one that changes.
 */
public class Main {

    public static void main(String[] args) {

        // ----------------------------------------------------
        // Section 4, Task 4 (DIP) — THE SWAP LINE.
        //
        // Comment one, uncomment the other. That is the entire cost of the
        // manager's storage migration. Bank.java, every account class and
        // every policy class are untouched by this decision.
        // ----------------------------------------------------

        // AccountRepository accountRepository = new InMemoryAccountRepository();
        AccountRepository accountRepository = new FileAccountRepository("accounts.txt");

        StatementGenerator statementGenerator = new StatementGenerator();

        // Same idea, one sprint earlier: swap this for `new SMSNotificationService()`.
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
        // accounts that really can pay out. No type check, no try/catch,
        // and no way for a fixed deposit to get in: the compiler refuses.
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
        System.out.println();

        // ----------------------------------------------------
        // Section 4, Task 2 (ISP) — capabilities, not class hierarchies.
        // ----------------------------------------------------

        System.out.println("---- ATM cash run ----");
        ATM atm = new ATM("ATM-42", 50000.0);

        // An ATM is not a BankAccount, yet it slots into the same two contracts
        // it genuinely shares with accounts. Under the old fat BankService it
        // would have had to stub transfer, printStatement and applyForLoan.
        List<Withdrawable> dispensers = new ArrayList<>();
        dispensers.add(atm);
        dispensers.add(savings);

        for (Withdrawable dispenser : dispensers) {
            System.out.println(dispenser.getClass().getSimpleName()
                    + ": dispense Rs. 100.0 -> " + dispenser.withdraw(100.0));
        }
        System.out.println("ATM cash on hand: Rs. " + atm.getCashOnHand());
        System.out.println();

        System.out.println("---- Transfer: Transferable -> Depositable ----");
        bank.transfer(salary, fd, 300.0);   // a fixed deposit may RECEIVE
        // bank.transfer(fd, salary, 300.0);  // <- will not compile: FD is not Transferable
        System.out.println();

        System.out.println("---- Loan: only LoanEligible types ----");
        bank.applyForLoan(current, 5000.0);
        // bank.applyForLoan(savings, 5000.0);  // <- will not compile: not LoanEligible
        System.out.println();

        // PIN enforcement from Lab 1 is still live on withdrawable types.
        // These two go straight to the account on purpose, to exercise the
        // PIN overload directly — so the repository is not updated by them.
        current.setPin(4321);
        System.out.println("PIN-protected withdrawal, wrong PIN -> " + current.withdraw(300.0, 1111));
        System.out.println("PIN-protected withdrawal, right PIN -> " + current.withdraw(300.0, 4321));
        System.out.println();

        // ----------------------------------------------------
        // Section 4, Task 4 — what actually landed in storage.
        // ----------------------------------------------------

        System.out.println("---- Repository contents (" 
                + accountRepository.getClass().getSimpleName() + ") ----");
        for (AccountRecord record : accountRepository.findAll()) {
            System.out.println("  " + record.toCsvLine());
        }
        System.out.println();

        System.out.println(bank.statementFor(fd));
        System.out.println();

        // ----------------------------------------------------
        // Section 2, Task 4b — same Bank class, different channel.
        // ----------------------------------------------------

        Bank smsBank = new Bank(accountRepository, new SMSNotificationService(), statementGenerator);
        smsBank.creditInterest(salary, new SalaryInterestPolicy());
    }
}
