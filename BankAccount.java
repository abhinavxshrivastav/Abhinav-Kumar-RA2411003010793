import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GreenLeaf Bank — BankAccount
 *
 * Task 1: reasons to change:
 *  1.Changes if the email provider changes (sendEmail)
 *  2.changes if the database changes (saveToDatabase)
 *  3.changes if the statement format changes (printStatement)
 *  4.Changes if the account rules change (deposit/ withdraw /minimum balance)
 *  5.Changes if the interest rates change (calculateInterest)
 *
 * Task 2 — job description:
 *  "Hold the details of one bank account and let money be put in or taken out of it."
 */
public class BankAccount {

    private int accountNumber;
    private String name;
    private int age;
    private double balance;
    private String status;
    private Integer pin;
    private String accountType; // "Savings" or "Current"

    // Kept here: this is the account's own factual history, not a
    // formatting or output concern (that's StatementGenerator's job).
    private List<String> transactionLog = new ArrayList<>();

    public BankAccount(int accountNumber, String name, int age, double balance, String accountType) {

        if (age < 18) {
            System.out.println("Age was below 18, correcting to 18");
            age = 18;
        }

        double minimumBalance = accountType.equals("Savings") ? 500.0 : 1000.0;
        if (balance < minimumBalance) {
            System.out.println("Initial balance below minimum, correcting to " + minimumBalance);
            balance = minimumBalance;
        }

        this.accountNumber = accountNumber;
        this.name = name;
        this.age = age;
        this.balance = balance;
        this.accountType = accountType;
        this.status = "Active";
        this.pin = null;
    }

    // ----------------------------------------------------
    // Account operations — no DB calls, no emails, nothing else mixed in
    // ----------------------------------------------------

    public boolean deposit(double amount) {

        if (!status.equals("Active")) {
            System.out.println("Account is not active");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Invalid deposit amount");
            return false;
        }

        balance += amount;
        transactionLog.add("DEPOSIT: Rs. " + amount + " | New balance: " + balance);

        return true;
    }

    /**
     * Section 3 (LSP): this used to be `public boolean withdraw(...)`, which
     * meant every subclass of BankAccount inherited the promise that money can
     * be taken out of it. FixedDepositAccount cannot keep that promise, and
     * the usual escape — override it and throw UnsupportedOperationException —
     * is precisely the LSP violation.
     *
     * So the mechanics stay here (one copy, no duplication) but they are
     * `protected`: only a subclass that genuinely CAN withdraw exposes them,
     * by implementing Withdrawable. FixedDepositAccount inherits this and
     * simply never opens it up, so no caller can reach it.
     */
    protected boolean performWithdrawal(double amount, Integer enteredPin) {

        if (!status.equals("Active")) {
            System.out.println("Account is not active");
            return false;
        }

        if (pin != null) {
            if (enteredPin == null || !enteredPin.equals(pin)) {
                System.out.println("Incorrect PIN");
                return false;
            }
        }

        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount");
            return false;
        }

        double minimumBalance = accountType.equals("Savings") ? 500.0 : 1000.0;
        if (balance - amount < minimumBalance) {
            System.out.println("Withdrawal would breach minimum balance");
            return false;
        }

        balance -= amount;
        transactionLog.add("WITHDRAW: Rs. " + amount + " | New balance: " + balance);

        return true;
    }

    public boolean closeAccount() {
        if (status.equals("Inactive")) return false;
        status = "Inactive";
        return true;
    }

    public boolean reopenAccount() {
        if (status.equals("Active")) return false;
        status = "Active";
        return true;
    }

    public boolean setPin(int newPin) {
        if (newPin >= 1000 && newPin <= 9999) {
            this.pin = newPin;
            return true;
        }
        return false;
    }

    public boolean verifyPin(int enteredPin) {
        return pin != null && pin.equals(enteredPin);
    }

    /**
     * Section 2 (OCP): superseded by InterestPolicy. Left here only to show
     * the failure mode — this chain was written before Salary accounts
     * existed, so a SalaryAccount falls through to the `else` and silently
     * earns 0.0. Interest is now calculated by passing an InterestPolicy to
     * Bank.creditInterest(), which never needs to know the account type.
     *
     * @deprecated use {@link InterestPolicy#calculate(double)} instead.
     */
    @Deprecated
    public double calculateInterest() {
        if (accountType.equals("Savings")) {
            return balance * 0.04;
        } else if (accountType.equals("Current")) {
            return balance * 0.01;
        } else {
            return 0.0;
        }
    }

    // Exposed read-only so StatementGenerator can format it without
    // BankAccount needing to know anything about formatting or printing.
    public List<String> getTransactionLog() {
        return Collections.unmodifiableList(transactionLog);
    }

    // ----------------------------------------------------
    // Getters
    // ----------------------------------------------------

    public int getAccountNumber() { return accountNumber; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getBalance() { return balance; }
    public String getStatus() { return status; }
    public String getAccountType() { return accountType; }
    public boolean hasPin() { return pin != null; }
}
