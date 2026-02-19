package org.poo.transactions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.banking.User;

import java.util.List;

public final class PrintTransactions {
    /**
     *
     * @param mapper
     * @param output
     * @param users
     * @param email
     * @param timestamp
     */
    public void handlePrintTransactions(final ObjectMapper mapper, final ArrayNode output,
                                        final List<User> users,
                                        final String email,
                                        final int timestamp) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "printTransactions");
        ArrayNode transactions = mapper.createArrayNode();

        for (User user : users) {
            if (user.getEmail().equals(email)) {
                for (Transaction transaction : user.getTransactionHistory().getTransactions()) {
                    ObjectNode transactionNode = mapper.createObjectNode();
                    //detaliile de baza ale tranzactiei
                    transactionNode.put("timestamp", transaction.getTimestamp());
                    transactionNode.put("description", transaction.getDescription());

                    //informatii pt transferuri
                    if (transaction.getSenderIBAN() != null) {
                        transactionNode.put("senderIBAN", transaction.getSenderIBAN());
                        transactionNode.put("receiverIBAN", transaction.getReceiverIBAN());
                        transactionNode.put("amount", transaction.getAmount());
                        transactionNode.put("transferType", transaction.getTransferType());
                    }

                    //informatii pt tranzactii cu comercianti
                    if (transaction.getCommerciant() != null) {
                        if (transaction.getAmount() != null) {
                            transactionNode.put("amount",
                                    Double.parseDouble(transaction.getAmount()));
                        }
                        transactionNode.put("commerciant", transaction.getCommerciant());
                    }

                    //informatii pt operatii cu cardul
                    if (transaction.getCard() != null) {
                        transactionNode.put("card", transaction.getCard());
                        transactionNode.put("cardHolder", transaction.getCardHolder());
                        transactionNode.put("account", transaction.getAccount());
                    }

                    if (transaction.getCurrency() != null) {
                        transactionNode.put("currency", transaction.getCurrency());
                    }

                    //cash withdrawal
                    if (transaction.getDescription().startsWith("Cash withdrawal")) {
                        double amount = Double.parseDouble(
                                transaction.getDescription().split(" ")[3]);
                        transactionNode.put("amount", amount);
                    }

                    //upgrade plan
                    if (transaction.getDescription().equals("Upgrade plan")) {
                        transactionNode.put("accountIBAN", transaction.getIban());
                        transactionNode.put("newPlanType", transaction.getNewPlan());
                    }

                    if (transaction.getDescription().equals("Interest rate income")) {
                        transactionNode.put("amount", Double.parseDouble(transaction.getAmount()));
                    }

                    if (transaction.getAccounts() != null) {
                        ArrayNode accounts = mapper.createArrayNode();
                        for (String account : transaction.getAccounts()) {
                            accounts.add(account);
                        }
                        transactionNode.set("involvedAccounts", accounts);
                    }

                    transactions.add(transactionNode);
                }
            }
        }

        node.set("output", transactions);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
