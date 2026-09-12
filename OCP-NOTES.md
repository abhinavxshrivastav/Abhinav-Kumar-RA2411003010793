# Section 2 — OCP: "New account types keep arriving"

## Task 1 (warm-up) — the if/else version and what a 4th type costs

`InterestCalculator.java` holds the before-picture. The prediction is written
as a comment at the top of that file: adding a "Salary" type forces edits to
**four existing methods** —

| # | File | Method | Why it has to change |
|---|------|--------|----------------------|
| 1 | `InterestCalculator.java` | `calculate(...)` | new `else if` branch for the rate |
| 2 | `InterestCalculator.java` | `describeRate(...)` | or the printed rate disagrees with the rate paid |
| 3 | `InterestCalculator.java` | `isSupported(...)` | or the account is rejected before it reaches `calculate` |
| 4 | `BankAccount.java` | `calculateInterest()` | carries a duplicate copy of the same chain |

The `else` fallback is the dangerous part: miss any one of these and a Salary
account silently earns `0.0` rather than failing loudly.

## Task 2 — the abstraction

`InterestPolicy` — one method, `double calculate(double balance)`. No account
types, no Strings, no branching.

## Task 3 — policies replace the chain

`SavingsInterestPolicy` (4%) and `CurrentInterestPolicy` (1%) implement it.
`Main` now credits interest through `Bank.creditInterest(account, policy)`,
which calls `policy.calculate(balance)`. The if/else chain is gone from the
live path; `BankAccount.calculateInterest()` is marked `@Deprecated` with a
comment explaining the failure it would have caused.

## Task 4 — Salary Accounts, and a swappable notification channel

New: `SalaryAccount extends BankAccount`, `SalaryInterestPolicy` (5%).
`Bank` takes `AccountRepository`, `NotificationService` and
`StatementGenerator` through its constructor, and `NotificationService` is now
an interface with `EmailNotificationService` and `SMSNotificationService`
behind it. Changing channel is a different constructor argument in `Main` —
`Bank` itself is never reopened.

## Task 5 (wrap-up) — the actual diff for the Salary Account requirement

**Brand new files (2):**
- `SalaryAccount.java`
- `SalaryInterestPolicy.java`

**Existing files edited (1):**
- `Main.java` — four lines of wiring, in the composition root, which is the
  one place that is *supposed* to change when a new type appears.

**Existing files opened and left alone:**
`InterestPolicy.java`, `SavingsInterestPolicy.java`, `CurrentInterestPolicy.java`,
`Bank.java`, `BankAccount.java`, `AccountRepository.java`,
`StatementGenerator.java`, `EmailNotificationService.java`.

> Adding Salary Accounts took two brand-new files and a four-line change to the
> composition root, with **zero existing policy classes modified** — compared to
> the four edits to working code that the `InterestCalculator` chain would have
> demanded.

## End-of-lab checkpoint

Adding a new account type or interest rate never requires editing an existing
policy class — it requires writing a new one and naming it in `Main`.

## Verified output

`javac -Xlint:all` compiles clean. Interest credited at run time:
Savings Rs. 52.00 on 1300 (4%), Current Rs. 50.00 on 5000 (1%),
Salary Rs. 150.00 on 3000 (5%), and the same Salary account through
`SMSNotificationService` Rs. 157.50 on 3150.
