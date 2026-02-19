package org.poo.banking;

import org.poo.transactions.Transaction;
import java.util.List;

public class AddInterest {
    /**
     *
     * @param users
     * @param account
     * @param timestamp
     */
    public void handleAddInterest(final List<User> users,
                                  final String account, final int timestamp) {
        for (User user : users) {
            for (Account acc : user.getAccounts()) {
                if (acc.getIBAN().equals(account)) {
                    if (acc.getType().equals("savings")) {
                        SavingsAccount savingsAcc = (SavingsAccount) acc;
                        //calcul al dobanzii
                        double interest = acc.getBalance() * savingsAcc.getInterestRate();
                        acc.setBalance(acc.getBalance() + interest);

                        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                                "Interest rate income")
                                .builderAmount(String.valueOf(interest))
                                .builderCurrency(acc.getCurrency())
                                .build();
                        user.getTransactionHistory().addTransaction(transaction);
                    }
                    break;
                }
            }
        }
    }
}
