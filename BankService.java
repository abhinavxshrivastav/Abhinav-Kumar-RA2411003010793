import java.util.List;

/**
 * GreenLeaf Bank — Section 4, Task 1 (warm-up): the fat interface.
 *
 * QUARANTINE NOTICE: nothing implements this. It is kept as the "before"
 * picture, alongside InterestCalculator (OCP) and LspViolationDemo (LSP).
 *
 * The ATM hardware team's complaint, written out as code:
 *
 *   public class ATM implements BankService {
 *
 *       private double cashOnHand;
 *
 *       // ---- the two it actually needs ----
 *
 *       public boolean deposit(double amount)  { cashOnHand += amount; return true; }
 *       public boolean withdraw(double amount) { cashOnHand -= amount; return true; }
 *
 *       // ---- the three it is FORCED to implement and does not need ----
 *
 *       public boolean transfer(BankAccount target, double amount) {
 *           throw new UnsupportedOperationException("An ATM does not move money between accounts");
 *       }
 *
 *       public String printStatement() {
 *           throw new UnsupportedOperationException("An ATM has no statement to print");
 *       }
 *
 *       public boolean applyForLoan(double amount) {
 *           throw new UnsupportedOperationException("An ATM does not underwrite loans");
 *       }
 *   }
 *
 * Three of five methods are dead weight, and the only way to fill them is the
 * override-and-throw that Section 3 spent the whole lab eliminating. That is
 * the link between the two principles: a fat interface does not merely annoy
 * the ATM team, it FORCES an LSP violation on anyone who implements it. The
 * ATM cannot be safely substituted for a BankService, because three fifths of
 * the contract are a lie.
 *
 * Split into Depositable / Withdrawable / Transferable / StatementProvider /
 * LoanEligible, the ATM implements two interfaces, writes two methods, and has
 * nothing to throw from.
 */
public interface BankService {

    boolean deposit(double amount);

    boolean withdraw(double amount);

    boolean transfer(BankAccount target, double amount);

    List<String> printStatement();

    boolean applyForLoan(double amount);
}
