/**
 * GreenLeaf Bank — Section 2, Task 4. Salary accounts earn 5%.
 *
 * This is the whole cost of the new requirement on the interest side: one
 * brand-new file. SavingsInterestPolicy and CurrentInterestPolicy were not
 * opened, and neither was InterestPolicy itself — contrast that with the
 * four-edit prediction written at the top of InterestCalculator.
 */
public class SalaryInterestPolicy implements InterestPolicy {

    private static final double RATE = 0.05;

    @Override
    public double calculate(double balance) {
        return balance * RATE;
    }
}
