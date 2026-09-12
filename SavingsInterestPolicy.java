/**
 * GreenLeaf Bank — Section 2, Task 3. Savings accounts earn 4%.
 *
 * One reason to change: the savings rate moves. Nothing else in the
 * codebase is disturbed when it does.
 */
public class SavingsInterestPolicy implements InterestPolicy {

    private static final double RATE = 0.04;

    @Override
    public double calculate(double balance) {
        return balance * RATE;
    }
}
