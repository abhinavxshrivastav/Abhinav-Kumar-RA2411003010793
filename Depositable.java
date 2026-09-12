/**
 * GreenLeaf Bank — Section 4, Task 2. "Money can be put into me."
 *
 * Every account type in the bank can honestly do this, including a fixed
 * deposit (it receives its credited interest). So can an ATM's cash cassette,
 * which is not an account at all — which is exactly why this is a capability
 * interface rather than a method on BankAccount.
 */
public interface Depositable {

    boolean deposit(double amount);
}
