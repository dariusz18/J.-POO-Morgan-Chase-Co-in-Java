package org.poo.banking;

import org.poo.transactions.Transaction;
import java.util.List;

public class WithdrawSavings {
    /**
     *
     * @param users
     * @param account
     * @param amount
     * @param currency
     * @param timestamp
     */
    public void handleWithdrawSavings(final List<User> users, final String account,
                                      final double amount, final String currency,
                                      final int timestamp) {
        //cautare user si account
        for (User user : users) {
            for (Account acc : user.getAccounts()) {
                if (acc.getIBAN().equals(account)) {
                    if (!acc.getType().equals("savings")) {
                        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                                "Account is not of type savings.").build();
                        user.getTransactionHistory().addTransaction(transaction);
                        return;
                    }
                    //verificare varsta
                    String[] parts = user.getBirthDate().split("-");
                    int year = Integer.parseInt(parts[0]);
                    if (!(Constants.DOIZERODOIPATRU  - year >= Constants.DOIUNU)) {
                        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                                "You don't have the minimum age required.").build();
                        user.getTransactionHistory().addTransaction(transaction);
                        return;
                    }

                    Account classic = null;
                    for (Account account1 : user.getAccounts()) {
                        if (account1.getType().equals("classic") &&
                                account1.getCurrency().equals(currency)) {
                            classic = account1;
                            break;
                        }
                    }
                    //verificare existenta cont clasic
                    if (classic == null) {
                        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                                "You do not have a classic account.").build();
                        user.getTransactionHistory().addTransaction(transaction);
                        return;
                    }
                    //convertim
                    double convert = amount;
                    if (!currency.equals(acc.getCurrency())) {
                        convert = Converter.getInstance().convert(currency,
                                acc.getCurrency(), amount);
                    }

                    if (acc.getBalance() < convert) {
                        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                                "Insufficient funds").build();
                        user.getTransactionHistory().addTransaction(transaction);
                        return;
                    }

                    acc.setBalance(acc.getBalance() - convert);
                    classic.setBalance(classic.getBalance() + convert);
                    //inregistrare tranzactie
                    Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                            "Savings withdrawal")
                            .builderSavingsAccount(acc.getIBAN())
                            .builderClassicAccount(classic.getIBAN())
                            .builderAmount(String.valueOf(amount))
                            .build();
                    user.getTransactionHistory().addTransaction(transaction);
                    return;
                }
            }
        }
    }
}
