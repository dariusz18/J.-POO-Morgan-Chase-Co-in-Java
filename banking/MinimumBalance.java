package org.poo.banking;

import java.util.List;

public final class MinimumBalance {
    /**
     *
     * @param users
     * @param account
     * @param amount
     */
    public void handleSetMinimumBalance(final List<User> users,
                                        final String account, final double amount) {
        for (User user : users) {
            for (Account bankAcc : user.getAccounts()) {
                if (bankAcc.getIBAN().equals(account)) {
                    bankAcc.setMinimumBalance(amount);
                }
            }
        }
    }
}
