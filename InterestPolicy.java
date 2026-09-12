/**
 * GreenLeaf Bank — Section 2, Task 2.
 *
 * The one thing every interest rule agrees on: given a balance, produce the
 * interest owed. No account types, no Strings, no if/else anywhere in sight.
 *
 * This interface is the "closed" half of Open/Closed — it is finished. Every
 * new account type opens a NEW class that implements it; no existing file
 * has to be reopened.
 */
public interface InterestPolicy {

    double calculate(double balance);
}
