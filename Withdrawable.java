/**
 * GreenLeaf Bank — Section 3, Task 4.
 *
 * The contract "money can be taken out of me on demand". It is deliberately
 * NOT on BankAccount: putting it there would make every account type promise
 * something a fixed deposit cannot deliver, and the only way out would be an
 * override that throws — the exact thing LSP forbids.
 *
 * A type implements this only if it can honestly honour it for every input
 * the caller is allowed to pass.
 */
public interface Withdrawable {

    boolean withdraw(double amount);
}
