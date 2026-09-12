/**
 * GreenLeaf Bank — fixed deposits earn 7%.
 *
 * Another one-file, zero-edit extension — the Section 2 checkpoint still
 * holding up two sections later.
 */
public class FixedDepositInterestPolicy implements InterestPolicy {

    private static final double RATE = 0.07;

    @Override
    public double calculate(double balance) {
        return balance * RATE;
    }
}
