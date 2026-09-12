# Section 3 — LSP: "A fixed deposit is not a withdrawable account"

## Task 1 — Square / Rectangle

In `LspViolationDemo.java`. Actual run output:

```
Rectangle: width=5, height=4 -> expected area 20.0, actual 20.0
Square:    width=5, height=4 -> expected area 20.0, actual 16.0
```

## Task 2 — why that breaks LSP

Code written against `Rectangle` assumes its two sides are independent: setting
the width leaves the height alone, so after `setWidth(5); setHeight(4)` the area
must be 20. `Square` cannot honour that assumption and stay square, so it
couples the setters and the same unchanged caller silently gets 16. LSP says a
subtype must be usable anywhere its supertype is expected *without the caller
knowing the difference* — and here the caller would have to ask "is this really
a square?" first, which is exactly the knowledge inheritance was supposed to
remove.

## Task 3 — the crash

`FixedDepositAccountV1 extends Account` overrides `withdraw()` to throw
`UnsupportedOperationException`. Looping over a `List<Account>` holding one
savings account and one fixed deposit:

```
SAV-1: withdrew Rs. 500.0, balance now Rs. 4500.0
CRASH: UnsupportedOperationException — Fixed deposits cannot be withdrawn before maturity
```

The first withdrawal committed, then the run died halfway. That is the worst
shape a failure can take: partial work done, no clean rollback point, and the
loop itself is blameless — it was written against `Account`, and `Account`
said withdrawal was allowed.

## Task 4 — the fix

- `Withdrawable` — one method, `boolean withdraw(double amount)`.
- `SavingsAccount`, `CurrentAccount` (and `SalaryAccount`, our own type from
  Section 2) implement it. All three genuinely can pay out.
- `FixedDepositAccount` does **not** implement it and has **no** `withdraw`
  method at all.
- `BankAccount.withdraw(...)` became `protected performWithdrawal(...)`, so the
  mechanics live in one place but only a subclass that can honestly withdraw
  exposes them.
- `Bank.withdraw` now takes `<T extends BankAccount & Withdrawable>`.
- The month-end run iterates `List<Withdrawable>`.

The payoff is that the bad call is no longer expressible. Verified against
`javac`:

```
list.add(fd);            error: incompatible types: FixedDepositAccount cannot be converted to Withdrawable
bank.withdraw(fd, 1.0);  error: inference variable T has incompatible bounds
fd.withdraw(1.0);        error: cannot find symbol — method withdraw(double)
```

Three compile errors instead of one production incident. The fixed deposit is
still a full account throughout: opened, saved, credited 7% interest, printed
on a statement. It just never claims to be withdrawable.

## Task 5 — why "implement Withdrawable and throw" is the wrong fix

It compiles because Java only checks the method *signature*, never the promise
behind it — a body of `throw new UnsupportedOperationException()` satisfies the
compiler completely. But LSP's substitution rule says an object of a subtype
must be usable everywhere the supertype is expected without changing the
correctness of the program, and a `FixedDepositAccount` that throws on every
single call fails that the instant it is substituted into the withdrawal loop.
The interface would become a lie: every caller would have to wrap `withdraw()`
in a `try/catch` or an `instanceof` check, which re-introduces exactly the
type-specific knowledge the interface existed to hide. All that approach really
achieves is moving the error from compile time, where it costs a red squiggle,
to run time, where it costs a half-finished batch job.

## End-of-lab checkpoint

No class in the live codebase overrides a method just to throw "not supported";
each type implements only the contracts it can truly fulfil.

The one exception is quarantined on purpose: `LspViolationDemo.java` holds the
counterexample so the crash stays reproducible. Nothing in the running program
references it, and the file says so at the top.

## Flagged, not fixed

Interest on the fixed deposit prints as `Rs. 7000.000000000001` — ordinary
`double` rounding, inherited from Lab 1's field types. Real banking code would
use `BigDecimal`. That is a change to every money field in every class, so it
is out of scope for this section; noted here rather than silently patched.
