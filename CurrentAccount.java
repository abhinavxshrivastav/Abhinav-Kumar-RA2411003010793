/**
 * GreenLeaf Bank — Current: 1% interest.
 *
 * The only type that is also LoanEligible — business current accounts are what
 * GreenLeaf lends against. A savings account declining to implement that
 * interface is not a limitation, it is an accurate statement about the product.
 */
public class CurrentAccount extends BankAccount
        implements Depositable, Withdrawable, Transferable, StatementProvider, LoanEligible {

    private static final double MAX_LOAN_MULTIPLE = 3.0;

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

    @Override
    public boolean applyForLoan(double amount) {
        if (amount <= 0 || amount > getBalance() * MAX_LOAN_MULTIPLE) {
            return false;
        }
        return deposit(amount);
    }
}
