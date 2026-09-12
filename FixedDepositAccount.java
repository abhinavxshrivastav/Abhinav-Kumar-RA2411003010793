/**
 * GreenLeaf Bank — Section 3, Task 4: the honest fixed deposit. 7% interest.
 *
 * Note what is NOT in this file: no `withdraw` method, no `implements
 * Withdrawable`, and — added in Section 4 — no Transferable either, since a
 * type that cannot pay out cannot transfer. A fixed deposit genuinely cannot
 * release funds before maturity, so it never makes the promise.
 *
 * The result is that `bank.withdraw(fd, 500.0)` does not throw at run time —
 * it does not COMPILE. The mistake is caught by javac instead of by a
 * customer. Compare with LspViolationDemo, where the same call compiles
 * cleanly and then blows up the whole loop.
 *
 * It still declares everything it CAN do: receive money (its credited
 * interest) and supply a statement.
 */
public class FixedDepositAccount extends BankAccount
        implements Depositable, StatementProvider {

    public FixedDepositAccount(int accountNumber, String name, int age, double balance) {
        super(accountNumber, name, age, balance, "FixedDeposit");
    }
}
