package org.poo.banking;

public class FeeCalculate {
    /**
     *
     * @param user
     * @param amount
     * @return
     */
    public static double calculateFee(final User user,
                                      final double amount) {
        //verificari ale planului pentru a returna comisionul
        if (user.getPlan().equals("student") || user.getPlan().equals("gold")) {
            return 0.0;
        }
        else if (user.getPlan().equals("standard")) {
            return amount * Constants.DOI;
        }
        if (user.getPlan().equals("silver")) {
            //conversie in RON
            double amountRON = Converter.getInstance()
                    .convert(user.getAccounts()
                            .get(0).getCurrency(),
                            "RON", amount);
            if (amountRON < Constants.CINCISUTE) {
                return 0.0;
            }
            return amount * Constants.UNU;
        }
        return 0.0;
    }
}

