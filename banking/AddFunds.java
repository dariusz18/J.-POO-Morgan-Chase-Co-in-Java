package org.poo.banking;

import java.util.List;

public final class AddFunds {
    /**
     * @param users
     * @param iban
     * @param amount
     */
    public void handleAddFunds(final List<User> users,
                               final String iban, final double amount) {
        for (User user : users) {
            for (Account acc : user.getAccounts()) {
                if (acc.getIBAN().equals(iban)) {
                    //adaugam fonduri
                    acc.setBalance(acc.getBalance() + amount);
                }
            }
        }
    }
}
