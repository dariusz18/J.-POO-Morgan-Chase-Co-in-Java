package org.poo.banking;

import org.poo.transactions.Transaction;
import org.poo.utils.Utils;
import java.util.List;
public final class CreateOneTimeCard {
    /**
     *
     * @param users
     * @param email
     * @param iban
     * @param timestamp
     */
    public void handleCreateOneTimeCard(final List<User> users, final String email,
                                        final String iban, final int timestamp) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                for (Account acc : user.getAccounts()) {
                    if (acc.getIBAN().equals(iban)) {
                        //la fel ca si CreateCard, doar ca e de tip one_time
                        String cardNumber = Utils.generateCardNumber();
                        Card card = new OneTimeCard(cardNumber);
                        acc.addCard(card);
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
