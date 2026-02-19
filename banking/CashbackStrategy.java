package org.poo.banking;

//interfata pt calculare cashback(folosind design ul Strategy)
public interface CashbackStrategy {
    /**
     *
     * @param account
     * @param user
     * @param commerciantType
     * @param amountRON
     * @return
     */
    double calculateCashback(Account account, User user,
                             String commerciantType,
                             double amountRON);
}
