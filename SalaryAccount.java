/**
 * GreenLeaf Bank — Salary: 5% interest. Added in Section 2 without opening a
 * single existing policy class.
 *
 * Section 3: a salary account can genuinely pay out on demand, so it honestly
 * implements Withdrawable. (The brief names only Savings and Current because
 * Salary is our own addition — excluding a type that CAN keep the contract
 * would be the same modelling error in reverse.)
 *
 * Note: BankAccount's constructor applies the non-Savings minimum balance
 * (Rs. 1000) to any type it does not recognise, so Salary accounts inherit
 * that floor. Minimum balance is an account rule, not an interest rule, so
 * it stays where it is rather than leaking into a policy class.
 */
public class SalaryAccount extends BankAccount
        implements Depositable, Withdrawable, Transferable, StatementProvider {

    public SalaryAccount(int accountNumber, String name, int age, double balance) {
        super(accountNumber, name, age, balance, "Salary");
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
