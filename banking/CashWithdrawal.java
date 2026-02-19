package org.poo.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.transactions.Transaction;

import java.util.List;

public class CashWithdrawal {
    /**
     *
     * @param mapper
     * @param output
     * @param users
     * @param email
     * @param cardNumber
     * @param amount
     * @param timestamp
     */
    public void handleCashWithdrawal(final ObjectMapper mapper, final ArrayNode output,
                                     final List<User> users, final String email,
                                     final String cardNumber, final double amount,
                                         final int timestamp) {
        //cautare user
        User user = null;
        for (User u : users) {
            if (u.getEmail().equals(email)) {
                user = u;
                break;
            }
        }
        //user negasit
        if (user == null) {
            ObjectNode responseNode = mapper.createObjectNode();
            responseNode.put("command", "cashWithdrawal");
            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "User not found");
            responseNode.set("output", outputNode);
            responseNode.put("timestamp", timestamp);
            output.add(responseNode);
            return;
        }
        //cautare cont si card
        Account foundAcc = null;
        Card foundCard = null;
        for (Account acc : user.getAccounts()) {
            Card card = acc.find(cardNumber);
            if (card != null) {
                foundAcc = acc;
                foundCard = card;
                break;
            }
        }
        //card negasit
        if (foundCard == null) {
            ObjectNode responseNode = mapper.createObjectNode();
            responseNode.put("command", "cashWithdrawal");
            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Card not found");
            responseNode.set("output", outputNode);
            responseNode.put("timestamp", timestamp);
            output.add(responseNode);
            return;
        }

        if (foundCard.getStatus().equals("frozen")) {
            Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                    "The card is frozen").build();
            user.getTransactionHistory().addTransaction(transaction);
            return;
        }
        //calcul fee && total && convertire
        double fee = FeeCalculate.calculateFee(user, amount);
        double total = amount + fee;

        if (!foundAcc.getCurrency().equals("RON")) {
            total = Converter.getInstance().convert("RON", foundAcc.getCurrency(), total);
        }

        if (foundAcc.getBalance() < total) {
            Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                    "Insufficient funds").build();
            user.getTransactionHistory().addTransaction(transaction);
            return;
        }
        //restul de cazuri
        if (foundAcc.getBalance() - total <= foundAcc.getMinimumBalance()) {
            Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                    "Cannot perform payment due to a minimum balance being set").build();
            user.getTransactionHistory().addTransaction(transaction);
            return;
        }

        foundAcc.setBalance(foundAcc.getBalance() - total);
        Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                "Cash withdrawal of " + amount).build();
        user.getTransactionHistory().addTransaction(transaction);
    }
}
