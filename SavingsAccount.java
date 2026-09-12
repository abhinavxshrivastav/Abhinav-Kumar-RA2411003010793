/**
 * GreenLeaf Bank — Section 3. Savings: withdrawable, 4% interest.
 */
public class SavingsAccount extends BankAccount implements Withdrawable {

    public SavingsAccount(int accountNumber, String name, int age, double balance) {
        super(accountNumber, name, age, balance, "Savings");
    }

    /** Honest implementation: this really can take money out. */
    @Override
    public boolean withdraw(double amount) {
        return performWithdrawal(amount, null);
    }

    /** PIN-aware form, kept from Lab 1. */
    public boolean withdraw(double amount, Integer enteredPin) {
        return performWithdrawal(amount, enteredPin);
    }
}
