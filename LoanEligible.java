/**
 * GreenLeaf Bank — Section 4, Task 2. "I can be lent against."
 *
 * Deliberately its own interface: loan underwriting is the fastest-changing,
 * most regulated part of a bank, and nothing that merely holds or moves money
 * should have to recompile when the lending rules move.
 */
public interface LoanEligible {

    boolean applyForLoan(double amount);
}
