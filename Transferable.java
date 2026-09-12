/**
 * GreenLeaf Bank — Section 4, Task 2. "Money can be moved from me to someone
 * who can receive it."
 *
 * Extends Withdrawable because a transfer is literally a withdrawal followed
 * by a deposit — a type that cannot pay out cannot transfer either, and saying
 * so in the type system means FixedDepositAccount can never be handed to this
 * contract by mistake.
 *
 * The default method is the whole implementation, so the three account types
 * that can transfer get it without copying six lines each.
 */
public interface Transferable extends Withdrawable {

    default boolean transferTo(Depositable target, double amount) {
        if (!withdraw(amount)) return false;
        return target.deposit(amount);
    }
}
