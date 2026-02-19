package org.poo.banking;

import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.List;

public final class CreateCard {
    /**
     *
     * @param users
     * @param email
     * @param iban
     * @param timestamp
     */
    public void handleCreateCard(final List<User> users, final String email,
                                 final String iban, final int timestamp) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                for (Account acc : user.getAccounts()) {
                    if (acc.getIBAN().equals(iban)) {
                        //generam si adaugam cardul
                        String cardNumber = Utils.generateCardNumber();
                        Card card = new Card(cardNumber);
                        acc.addCard(card);
                        //inregistram tranzactia
                        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                                "New card created").builderCard(cardNumber)
                                .builderCardHolder(email).builderAccount(iban).build();
                        user.getTransactionHistory().addTransaction(transaction);
                    }
                }
            }
        }
    }
}
