package org.poo.banking;

public class NrOfTransactions implements CashbackStrategy {
    /**
     *
     * @param account
     * @param user
     * @param commerciantType
     * @param amount
     * @return
     */
    @Override
    public double calculateCashback(final Account account,
                                    final User user,
                                    final String commerciantType,
                                    final double amount) {
         double cashback = 0.0;
        //daca e comerciant de tip food si
        // se efectueaza mai mult de doua tranzactii cashback ul este 2%
        if (!account.cashbackUsed("Food") && account.getCount("Food") >= 2) {
            cashback = Constants.DOI;
            account.markTrue("Food");
        } else if (!account.cashbackUsed("Clothes") && account.getCount("Clothes") >= Constants.CINCI_MARE) {
            cashback = Constants.CINCI;
            account.markTrue("Clothes");
        } else if (!account.cashbackUsed("Tech") && account.getCount("Tech") >= Constants.ZECE_MARE) {
            cashback = Constants.ZECE;
            account.markTrue("Tech");
        }
        return amount * cashback;
    }
}
