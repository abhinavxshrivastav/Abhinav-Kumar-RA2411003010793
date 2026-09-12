/**
 * GreenLeaf Bank — Section 2, Task 4.
 *
 * Lab 1 never grew a separate `Account` abstraction, so per the brief this
 * reuses BankAccount as the base class.
 *
 * Note: BankAccount's constructor applies the non-Savings minimum balance
 * (Rs. 1000) to any type it does not recognise, so Salary accounts inherit
 * that floor. Minimum balance is an account rule, not an interest rule, so
 * it stays where it is rather than leaking into a policy class.
 */
public class SalaryAccount extends BankAccount {

    public SalaryAccount(int accountNumber, String name, int age, double balance) {
        super(accountNumber, name, age, balance, "Salary");
    }
}
