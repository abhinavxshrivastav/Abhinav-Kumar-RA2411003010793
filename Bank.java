/**
 * GreenLeaf Bank — the service layer.
 *
 * Section 4 checkpoint: every field below is an interface or a pure formatter.
 * Bank contains no `new` of a concrete collaborator anywhere — it is handed
 * what it needs and never chooses. Swapping in-memory storage for a text file,
 * or email for SMS, does not touch this file.
 *
 * The method signatures state their requirements as intersections: "a
 * BankAccount that is also Depositable", "a BankAccount that is also
 * Withdrawable". Anything that cannot honestly do the job is rejected by the
 * compiler rather than at run time.
 */
public class Bank {

    private final AccountRepository accountRepository;       // interface
    private final NotificationService notificationService;   // interface
    private final StatementGenerator statementGenerator;     // pure formatter

    public Bank(AccountRepository accountRepository,
                NotificationService notificationService,
                StatementGenerator statementGenerator) {
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
        this.statementGenerator = statementGenerator;
    }

    public void open(BankAccount account) {
        accountRepository.save(account);
        notificationService.send("To: " + account.getName()
                + " | Account opened with balance Rs. " + account.getBalance());
    }

    public <T extends BankAccount & Depositable> void deposit(T account, double amount) {
        if (!account.deposit(amount)) return;
        accountRepository.save(account);
        notificationService.send("To: " + account.getName()
                + " | Your deposit of Rs. " + amount
                + " was successful. New balance: " + account.getBalance());
    }

    /**
     * Section 3 (LSP): the parameter type is the intersection "a BankAccount
     * that is also Withdrawable". Bank needs the account half for saving and
     * notifying, and the Withdrawable half for the operation itself.
     *
     * Passing a FixedDepositAccount here is a compile error, not a run-time
     * exception — the design makes the bad call unsayable.
     */
    public <T extends BankAccount & Withdrawable> void withdraw(T account, double amount) {
        if (!account.withdraw(amount)) return;
        accountRepository.save(account);
        notificationService.send("To: " + account.getName()
                + " | Your withdrawal of Rs. " + amount
                + " was successful. New balance: " + account.getBalance());
    }

    /**
     * Section 4 (ISP): the source must be Transferable, the destination merely
     * Depositable. Stating the two sides separately means a fixed deposit can
     * receive a transfer but can never be the source of one.
     */
    public <T extends BankAccount & Transferable, U extends BankAccount & Depositable>
    void transfer(T from, U to, double amount) {

        if (!from.transferTo(to, amount)) {
            System.out.println("Transfer declined");
            return;
        }

        accountRepository.save(from);
        accountRepository.save(to);
        notificationService.send("To: " + from.getName()
                + " | Rs. " + amount + " transferred to " + to.getName()
                + ". New balance: " + from.getBalance());
    }

    /**
     * The OCP payoff. This method depends on InterestPolicy, not on a list of
     * account types — SalaryInterestPolicy and FixedDepositInterestPolicy both
     * arrived after it was written and it did not change by one character.
     */
    public <T extends BankAccount & Depositable> double creditInterest(T account, InterestPolicy policy) {
        double interest = policy.calculate(account.getBalance());
        account.deposit(interest);
        accountRepository.save(account);
        notificationService.send("To: " + account.getName()
                + " | Interest of Rs. " + interest
                + " credited. New balance: " + account.getBalance());
        return interest;
    }

    /**
     * Section 4 (ISP): only a LoanEligible type can be passed. There is no
     * `if (account instanceof CurrentAccount)` anywhere — the interface list
     * on each account class is the whole eligibility rule.
     */
    public <T extends BankAccount & LoanEligible> boolean applyForLoan(T account, double amount) {

        if (!account.applyForLoan(amount)) {
            notificationService.send("To: " + account.getName()
                    + " | Loan application for Rs. " + amount + " was declined.");
            return false;
        }

        accountRepository.save(account);
        notificationService.send("To: " + account.getName()
                + " | Loan of Rs. " + amount + " approved and disbursed. New balance: "
                + account.getBalance());
        return true;
    }

    /** Takes the capability, not the class: anything that can answer the four
     *  statement questions can be printed, account or not. */
    public String statementFor(StatementProvider account) {
        return statementGenerator.generate(account);
    }
}
