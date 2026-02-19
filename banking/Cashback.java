package org.poo.banking;

public class Cashback {
    private static final CashbackStrategy TRANSACTION = new NrOfTransactions();
    private static final CashbackStrategy SPENDING = new SpendingThresholdCashback();

    /**
     *
     * @param account
     * @param user
     * @param commerciantType
     * @param cashbackStrategy
     * @param amountRON
     * @return
     */
    public static double calculateCashback(final Account account, final User user,
                                           final String commerciantType,
                                           final String cashbackStrategy,
                                           final double amountRON) {
        //pe baza cashbackstrategy calculam cashback ul
        if (cashbackStrategy.equals("nrOfTransactions")) {
            return TRANSACTION.calculateCashback(account, user, commerciantType, amountRON);
        } else if (cashbackStrategy.equals("spendingThreshold")) {
            return SPENDING.calculateCashback(account, user, commerciantType, amountRON);
        } else {
            return 0.0;
        }
    }
}
