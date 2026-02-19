package org.poo.banking;

public class AccountFactory {
    /**
     *
     * @param type
     * @param iban
     * @param currency
     * @param interestRate
     * @return
     */
    public Account createAccount(final String type,
                                 final String iban,
                                 final String currency,
                                 final Double interestRate) {
        //verificam tipurile conturilor si pe baza tipulul returnam un cont de tipul respectiv
        switch (type) {
            case "savings":
                SavingsAccount savingsAccount = new SavingsAccount(iban, currency);
                if (interestRate != null) {
                    savingsAccount.setInterestRate(interestRate);
                }
                return savingsAccount;
            case "classic":
                return new Account(iban, currency, "classic");
            case "business":
                return new Account(iban, currency, "business");
                default:
                    return null;
        }
    }
}
