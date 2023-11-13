package it.unibo.bank.impl;

import it.unibo.bank.api.AccountHolder;
import it.unibo.bank.api.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.unibo.bank.impl.SimpleBankAccount.*;
import static it.unibo.bank.impl.StrictBankAccount.TRANSACTION_FEE;
import static org.junit.jupiter.api.Assertions.*;

public class TestStrictBankAccount {

    private final static int INITIAL_AMOUNT = 100;

    // 1. Create a new AccountHolder and a StrictBankAccount for it each time tests are executed.
    private AccountHolder mRossi;
    private BankAccount bankAccount;

    @BeforeEach
    public void setUp() {
        this.mRossi = new AccountHolder("Mario", "Rossi", 1);
        this.bankAccount = new StrictBankAccount(mRossi, INITIAL_AMOUNT);
    }

    // 2. Test the initial state of the StrictBankAccount
    @Test
    public void testInitialization() {
        assertEquals(INITIAL_AMOUNT, this.bankAccount.getBalance());
        assertEquals(0, this.bankAccount.getTransactionsCount());
        assertEquals(this.mRossi, this.bankAccount.getAccountHolder());
    }


    // 3. Perform a deposit of 100€, compute the management fees, and check that the balance is correctly reduced.
    @Test
    public void testManagementFees() {
        this.bankAccount.deposit(this.mRossi.getUserID(), INITIAL_AMOUNT);
        assertEquals(this.bankAccount.getBalance(), INITIAL_AMOUNT + INITIAL_AMOUNT);
        final double initialBalance = this.bankAccount.getBalance();
        final double feeAmount = MANAGEMENT_FEE + this.bankAccount.getTransactionsCount() * TRANSACTION_FEE;
        assertTrue(this.bankAccount.getTransactionsCount() > 0);
        this.bankAccount.chargeManagementFees(this.mRossi.getUserID());
        assertEquals(0, this.bankAccount.getTransactionsCount());
        assertEquals(initialBalance - feeAmount, this.bankAccount.getBalance());
    }

    // 4. Test the withdraw of a negative value
    @Test
    public void testNegativeWithdraw() {
        try {
            this.bankAccount.withdraw(this.mRossi.getUserID(), -this.bankAccount.getBalance());
            fail("Negative withdraws have been allowed");
        } catch (final IllegalArgumentException e) {
            assertEquals("Cannot withdraw a negative amount", e.getMessage());
        }
    }

    // 5. Test withdrawing more money than it is in the account
    @Test
    public void testWithdrawingTooMuch() {
        try {
            this.bankAccount.withdraw(this.mRossi.getUserID(), this.bankAccount.getBalance() + 1);
            fail("Withdraw amount greater than balance have been allowed");
        } catch (final IllegalArgumentException e) {
            assertEquals("Insufficient balance", e.getMessage());
        }
    }
}
