package org.poo.transactions;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {
    //lista ce stocheaza tranzactiile
    private final List<Transaction> transactions;

    public TransactionHistory() {
        this.transactions = new ArrayList<>();
    }

    /**
     *
     * @param transaction
     */
    public void addTransaction(final Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }
}
