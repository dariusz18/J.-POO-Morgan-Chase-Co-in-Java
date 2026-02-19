package org.poo.banking;

import org.poo.transactions.Transaction;

import java.util.List;

public final class DeleteCard {
    /**
     *
     * @param users
     * @param cardNumber
     * @param timestamp
     */
    public void handleDeleteCard(final List<User> users,
                                 final String cardNumber,
                                 final int timestamp) {
        for (User user : users) {
            for (Account acc : user.getAccounts()) {
                for (int i = 0; i < acc.getCards().size(); i++) {
                    Card card = acc.getCards().get(i);
                    if (card.getCardNumber().equals(cardNumber)) {
                        //stergere card
                        acc.getCards().remove(i);
                        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                                "The card has been destroyed")
                                .builderCard(cardNumber).builderCardHolder(user.getEmail())
                                .builderAccount(acc.getIBAN()).build();
                        user.getTransactionHistory().addTransaction(transaction);
                        break;
                    }
                }
            }
        }
    }
}
