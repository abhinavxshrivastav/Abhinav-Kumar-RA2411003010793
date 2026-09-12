/**
 * GreenLeaf Bank — Section 3. Current: withdrawable, 1% interest.
 */
public class CurrentAccount extends BankAccount implements Withdrawable {

    public CurrentAccount(int accountNumber, String name, int age, double balance) {
        super(accountNumber, name, age, balance, "Current");
    }

    @Override
    public boolean withdraw(double amount) {
        return performWithdrawal(amount, null);
    }

    /** PIN-aware form, kept from Lab 1. */
    public boolean withdraw(double amount, Integer enteredPin) {
        return performWithdrawal(amount, enteredPin);
    }
}
