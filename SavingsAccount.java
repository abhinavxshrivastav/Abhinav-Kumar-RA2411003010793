/**
 * GreenLeaf Bank — Savings: 4% interest.
 *
 * Section 4 (ISP): the capability list is the class's honest job description.
 * Depositable and StatementProvider are satisfied by methods inherited from
 * BankAccount; Withdrawable is implemented below; Transferable supplies its
 * own default. Nothing here is a stub.
 */
public class SavingsAccount extends BankAccount
        implements Depositable, Withdrawable, Transferable, StatementProvider {

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
