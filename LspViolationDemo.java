import java.util.ArrayList;
import java.util.List;

/**
 * GreenLeaf Bank — Section 3, Tasks 1 and 3: the two "before" pictures.
 *
 * QUARANTINE NOTICE: this file is the counterexample, kept so the crash can
 * be reproduced on demand. It is the ONLY place in the repo where a method is
 * overridden just to throw "not supported", and none of these classes are
 * used by the real program. The end-of-lab checkpoint holds for the live
 * codebase — see FixedDepositAccount, which has no withdraw method at all.
 *
 * Run with:  java -cp .dist LspViolationDemo
 */
public class LspViolationDemo {

    // ------------------------------------------------------------------
    // Task 1 — the classic Square / Rectangle
    // ------------------------------------------------------------------

    static class Rectangle {

        protected double width;
        protected double height;

        void setWidth(double width) { this.width = width; }
        void setHeight(double height) { this.height = height; }
        double area() { return width * height; }
    }

    /**
     * "A square IS-A rectangle" is true in geometry and false in code: a
     * Rectangle promises that setting one side leaves the other alone, and
     * Square cannot keep that promise and stay square.
     */
    static class Square extends Rectangle {

        @Override
        void setWidth(double width) {
            this.width = width;
            this.height = width;
        }

        @Override
        void setHeight(double height) {
            this.height = height;
            this.width = height;
        }
    }

    static void rectangleDemo() {

        System.out.println("--- Task 1: Square / Rectangle ---");

        List<Rectangle> shapes = new ArrayList<>();
        shapes.add(new Rectangle());
        shapes.add(new Square());

        for (Rectangle shape : shapes) {
            // Calling code written against Rectangle, doing nothing unusual.
            shape.setWidth(5);
            shape.setHeight(4);
            System.out.println("  " + shape.getClass().getSimpleName()
                    + ": width=5, height=4 -> expected area 20.0, actual " + shape.area());
        }
    }

    // ------------------------------------------------------------------
    // Task 3 — withdraw() living on the base type
    // ------------------------------------------------------------------

    /** The "before" base: it promises that EVERY account can be withdrawn from. */
    abstract static class Account {

        protected final String id;
        protected double balance;

        Account(String id, double balance) {
            this.id = id;
            this.balance = balance;
        }

        boolean withdraw(double amount) {
            if (amount <= 0 || amount > balance) return false;
            balance -= amount;
            System.out.println("  " + id + ": withdrew Rs. " + amount
                    + ", balance now Rs. " + balance);
            return true;
        }
    }

    static class SavingsAccountV1 extends Account {
        SavingsAccountV1(String id, double balance) { super(id, balance); }
    }

    /** The violation: inherits the promise, then refuses to keep it. */
    static class FixedDepositAccountV1 extends Account {

        FixedDepositAccountV1(String id, double balance) { super(id, balance); }

        @Override
        boolean withdraw(double amount) {
            throw new UnsupportedOperationException(
                    "Fixed deposits cannot be withdrawn before maturity");
        }
    }

    static void brokenWithdrawalRun() {

        System.out.println("--- Task 3: withdraw() over List<Account> ---");

        List<Account> accounts = new ArrayList<>();
        accounts.add(new SavingsAccountV1("SAV-1", 5000.0));
        accounts.add(new FixedDepositAccountV1("FD-1", 100000.0));

        for (Account account : accounts) {
            account.withdraw(500.0);
        }

        System.out.println("  Month-end withdrawal run finished."); // never printed
    }

    public static void main(String[] args) {

        rectangleDemo();
        System.out.println();

        try {
            brokenWithdrawalRun();
        } catch (UnsupportedOperationException e) {
            System.out.println("  CRASH: " + e.getClass().getSimpleName() + " — " + e.getMessage());
            System.out.println();
            System.out.println("  Note what did NOT happen: the savings withdrawal above succeeded,");
            System.out.println("  then one bad element killed the whole run. Nothing is wrong with");
            System.out.println("  the loop — it was written against Account, and Account said this");
            System.out.println("  was allowed. The lie is in the type, not in the caller.");
        }
    }
}
