/**
 * GreenLeaf Bank — Section 3, Task 4: the honest fixed deposit.
 *
 * Note what is NOT in this file: there is no `withdraw` method, and no
 * `implements Withdrawable`. A fixed deposit genuinely cannot pay out before
 * maturity, so it never makes the promise in the first place.
 *
 * The result is that `bank.withdraw(fd, 500.0)` does not throw at run time —
 * it does not COMPILE. The mistake is caught by javac instead of by a
 * customer. Compare with LspViolationDemo, where the same call compiles
 * cleanly and then blows up the whole loop.
 *
 * It still does everything an account can honestly do: hold a balance, be
 * saved, receive credited interest, and appear on a statement.
 */
public class FixedDepositAccount extends BankAccount {

    public FixedDepositAccount(int accountNumber, String name, int age, double balance) {
        super(accountNumber, name, age, balance, "FixedDeposit");
    }
}
