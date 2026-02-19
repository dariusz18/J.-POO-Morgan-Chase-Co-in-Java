package org.poo.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.transactions.Transaction;
import java.util.List;

public class UpgradePlan {
    private double getFee(String curent, String next) {
        if (curent.equals("standard") && next.equals("silver")) {
            return Constants.OSUTA;
        } else if (curent.equals("student") && next.equals("silver")) {
            return Constants.OSUTA;
        } else if (curent.equals("silver") && next.equals("gold")) {
            return Constants.DOICINCISUTE;
        } else if (curent.equals("standard") && next.equals("gold")) {
            return Constants.TREICINCISUTE;
        } else if (curent.equals("student") && next.equals("gold")) {
            return Constants.TREICINCISUTE;
        } else if (curent.equals("student") && next.equals("gold")) {
            return Constants.TREICINCISUTE;
        }
        return 0.0;
    }

    /**
     *
     * @param mapper
     * @param output
     * @param users
     * @param acc
     * @param plan
     * @param time
     */
    public void handleUpgradePlan(final ObjectMapper mapper, final ArrayNode output,
                                  final List<User> users, final String acc,
                                  final String plan, final int time) {
        User user = null;
        Account account = null;
        //cautare user si account
        for (User u : users) {
            for (Account act : u.getAccounts()) {
                if (act.getIBAN().equals(acc)) {
                    user = u;
                    account = act;
                }
            }
        }
        //verificar daca exista
        if (account == null || user == null) {
            ObjectNode node = mapper.createObjectNode();
            node.put("command", "upgradePlan");
            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", time);
            outputNode.put("description", "Account not found");
            node.set("output", outputNode);
            node.put("timestamp", time);
            output.add(node);
            return;
        }
        //verificare daca utilizatorul are deja planul dorit
        if (user.getPlan().equals(plan)) {
            Transaction transaction = new Transaction.TransactionBuilder(time,
                    "The user already has the " + plan + " plan.")
                    .builderAccountIBAN(acc)
                    .build();
            user.getTransactionHistory().addTransaction(transaction);
            return;
        }

        if (!user.upgrade(plan)) {
            Transaction transaction = new Transaction.TransactionBuilder(time,
                    "You cannot downgrade your plan.")
                    .builderAccountIBAN(acc)
                    .build();
            user.getTransactionHistory().addTransaction(transaction);
            return;
        }

        //fee ul pentru upgrade
        double fee = getFee(user.getPlan(), plan);
        double converter;//convertia in RON
        if (!account.getCurrency().equals("RON")) {
            converter = Converter.getInstance().convert("RON", account.getCurrency(), fee);
        } else {
            converter = fee;
        }

        if (account.getBalance() < converter) {
            Transaction t = new Transaction.TransactionBuilder(time,
                    "Insufficient funds")
                    .builderAccountIBAN(acc)
                    .build();
            user.getTransactionHistory().addTransaction(t);
            return;
        }

        account.setBalance((account.getBalance() - converter));
        user.setPlan(plan);

        Transaction transaction = new Transaction.TransactionBuilder(time, "Upgrade plan")
                .builderAccountIBAN(acc)
                .builderNewPlanType(plan)
                .build();
        user.getTransactionHistory().addTransaction(transaction);
    }
}
