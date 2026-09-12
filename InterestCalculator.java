/**
 * GreenLeaf Bank — Section 2, Task 1 (warm-up): the "before" picture.
 *
 * This is interest calculation done with an if/else chain on a String.
 * It is kept in the repo on purpose as the thing OCP is meant to fix —
 * nothing in the running program calls it any more. See InterestPolicy
 * and its implementations for the "after".
 *
 * PREDICTION — to add a 4th account type ("Salary", 5%) I would have to
 * open and edit existing, already-working code in four places:
 *
 *   1. calculate(...)      — add an `else if (accountType.equals("Salary"))`
 *                            branch (line 38 below).
 *   2. describeRate(...)   — add the matching branch (line 51). Miss this one
 *                            and the rate printed on the statement quietly
 *                            disagrees with the rate actually paid.
 *   3. isSupported(...)    — add "Salary" to the list (line 62), or the account
 *                            is rejected before it ever reaches calculate().
 *   4. BankAccount.calculateInterest() — a different file carrying its own copy
 *                            of the same chain, so the edit has to be made twice.
 *
 * That is 3 methods here + 1 method in another file: four edits to signed-off
 * code for one additive requirement. The `else` fallback makes it worse — miss
 * any one of them and a Salary account silently earns 0.0 instead of failing
 * loudly, which is a bug nobody notices until a customer complains.
 */
public class InterestCalculator {

    public double calculate(String accountType, double balance) {

        if (accountType.equals("Savings")) {
            return balance * 0.04;
        } else if (accountType.equals("Current")) {
            return balance * 0.01;
        } else {
            // Every unknown type lands here and earns nothing, in silence.
            return 0.0;
        }
    }

    public String describeRate(String accountType) {

        if (accountType.equals("Savings")) {
            return "4%";
        } else if (accountType.equals("Current")) {
            return "1%";
        } else {
            return "0%";
        }
    }

    public boolean isSupported(String accountType) {
        return accountType.equals("Savings") || accountType.equals("Current");
    }
}
