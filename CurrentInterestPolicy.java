/**
 * GreenLeaf Bank — Section 2, Task 3. Current accounts earn 1%.
 */
public class CurrentInterestPolicy implements InterestPolicy {

    private static final double RATE = 0.01;

    @Override
    public double calculate(double balance) {
        return balance * RATE;
    }
}
