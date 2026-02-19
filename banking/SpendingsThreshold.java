package org.poo.banking;

class SpendingThresholdCashback implements CashbackStrategy {
    /**
     *
     * @param account
     * @param user
     * @param commerciantType
     * @param amountRON
     * @return
     */
    @Override
    public double calculateCashback(final Account account,
                                    final User user,
                                    final String commerciantType,
                                    final double amountRON) {
        if (amountRON <= 0) {
            return 0.0;
        }
        double total = account.getTotal();
        double cashback = 0.0;
        String plan = user.getPlan();

        if (total >= Constants.CINCISUTE) {
            if (plan.equals("gold")) {
                cashback = Constants.SAPTE;
            } else if (plan.equals("silver")) {
                cashback = Constants.CINCI;
            } else {
                cashback = Constants.DOICINCI;
            }
        } else if (total >= Constants.TREISUTE) {
            if (plan.equals("gold")) {
                cashback = Constants.CINCICINCI;
            } else if (plan.equals("silver")) {
                cashback = Constants.PATRU;
            } else if (plan.equals("standard") || plan.equals("student")) {
                cashback = Constants.DOI;
            }
        } else if (total >= Constants.OSUTA) {
            if (plan.equals("gold")) {
                cashback = Constants.CINCI;
            } else if (plan.equals("silver")) {
                cashback = Constants.TREI;
            } else if (plan.equals("standard") || plan.equals("student")) {
                cashback = Constants.UNU;
            }
        }

        return amountRON * cashback;
    }
}
