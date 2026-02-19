package org.poo.banking;

import org.poo.transactions.Transaction;
import org.poo.utils.Utils;
import java.util.List;

public final class AddAccount {
    private final AccountFactory accountFactory;

    public AddAccount() {
        this.accountFactory = new AccountFactory();
    }

    /**
     *
     * @param users
     * @param email
     * @param currency
     * @param type
     * @param timestamp
     * @param interestRate
     */

    public void handleAddAccount(final List<User> users,
                                 final String email, final String currency,
                                 final String type, final int timestamp,
                                 final Double interestRate) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                String iban = Utils.generateIBAN();
                //folosim factory-ul pentru a crea contul
                Account account = accountFactory.createAccount(type, iban, currency, interestRate);
                user.addAccount(account);

                //inregistrez tranzactia
                Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                        "New account created").build();
                user.getTransactionHistory().addTransaction(transaction);
                break;
            }
        }
    }
}
