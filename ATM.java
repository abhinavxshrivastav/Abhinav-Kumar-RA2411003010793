/**
 * GreenLeaf Bank — Section 4, Task 2: the ATM team's complaint, resolved.
 *
 * Two interfaces, two methods, nothing thrown. Compare with the commented-out
 * version in BankService.java, which was forced to stub three methods it has
 * no business having.
 *
 * Note that an ATM is not a BankAccount and never was — it is a cash cassette
 * with a keypad. Capability interfaces let it share exactly the two contracts
 * it genuinely shares with accounts, and nothing more.
 */
public class ATM implements Depositable, Withdrawable {

    private final String atmId;
    private double cashOnHand;

    public ATM(String atmId, double cashOnHand) {
        this.atmId = atmId;
        this.cashOnHand = cashOnHand;
    }

    /** Cash loaded into the machine by the replenishment team. */
    @Override
    public boolean deposit(double amount) {
        if (amount <= 0) return false;
        cashOnHand += amount;
        return true;
    }

    /** Cash dispensed to a customer. */
    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > cashOnHand) return false;
        cashOnHand -= amount;
        return true;
    }

    public String getAtmId() { return atmId; }
    public double getCashOnHand() { return cashOnHand; }
}
