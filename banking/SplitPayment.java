package org.poo.banking;

import org.poo.transactions.Transaction;
import org.poo.fileio.ExchangeInput;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class SplitPayment {
    /**
     *
     * @param users
     * @param accSplit
     * @param amount
     * @param currency
     * @param timestamp
     * @param exchange
     */
    public void handleSplitPayment(final List<User> users, final List<String> accSplit,
                                  final double amount, final String currency, final int timestamp,
                                  final List<ExchangeInput> exchange) {
        // initializam ratele in converter
        Converter.getInstance().initializare(exchange.toArray(new ExchangeInput[0]));
        double splitAmount = amount / accSplit.size();
        //gasim conturile si proprietarii
        Map<String, Account> acc = findAcc(users, accSplit);
        Map<String, User> propr = findPropr(users, accSplit);
        //verificam daca plata poate fii efectuata
        if (verificare(acc, splitAmount, currency)) {
            split(acc, propr, accSplit, amount, splitAmount,
                    currency, timestamp);
        } else {
            fail(propr, accSplit, timestamp);
        }
    }

    private Map<String, Account> findAcc(final List<User> users, final List<String> accSplit) {
        Map<String, Account> acc = new HashMap<>();
        for (String iban : accSplit) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().equals(iban)) {
                        acc.put(iban, account);
                        break;
                    }
                }
            }
        }
        return acc;
    }

    private Map<String, User> findPropr(final List<User> users, final List<String> accSplit) {
        Map<String, User> propr = new HashMap<>();
        for (String iban : accSplit) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getIBAN().equals(iban)) {
                        propr.put(iban, user);
                        break;
                    }
                }
            }
        }
        return propr;
    }

    private boolean verificare(final Map<String, Account> accMap,
                               final double splitAmount,
                               final String currency) {
        for (Account account : accMap.values()) {
            double amount = Converter.getInstance().convert(currency,
                    account.getCurrency(), splitAmount);
            if (account.getBalance() < amount) {
                return false;
            }
        }
        return true;
    }

    private void split(final Map<String, Account> accMap, final Map<String, User> proprMap,
                       final List<String> accSplit, final double total,
                       final double splitAmount, final String currency,
                       final int timestamp) {
        for (String iban : accSplit) {
            Account account = accMap.get(iban);
            User propr = proprMap.get(iban);
            double amount = Converter.getInstance().convert(currency,
                    account.getCurrency(), splitAmount);

            account.setBalance(account.getBalance() - amount);

            Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                    String.format("Split payment of %.2f %s", total, currency))
                    .builderAmount(String.format("%.20f", splitAmount))
                    .builderCurrency(currency)
                    .builderAccounts(accSplit).build();

            propr.getTransactionHistory().addTransaction(transaction);
        }
    }

    private void fail(final Map<String, User> proprMap,
                      final List<String> accSplit, final int timestamp) {
        for (String iban : accSplit) {
            User propr = proprMap.get(iban);
            Transaction fail = new Transaction.TransactionBuilder(timestamp,
                    "Insufficient funds for split payment").build();
            propr.getTransactionHistory().addTransaction(fail);
        }
    }
}
