package org.poo.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.transactions.Transaction;

import java.util.List;

public final class CardStatus {
    /**
     *
     * @param mapper
     * @param output
     * @param users
     * @param cardNumber
     * @param timestamp
     */
    public void handleCheckCardStatus(final ObjectMapper mapper,
                                      final ArrayNode output, final List<User> users,
                                     final String cardNumber, final int timestamp) {

        ObjectNode node = mapper.createObjectNode();
        node.put("command", "checkCardStatus");
        ObjectNode outputNode = mapper.createObjectNode();
        int found = 0;

        //caut cardul si daca il gasim verificam starea lui
        for (User user : users) {
            for (Account acc : user.getAccounts()) {
                for (Card card : acc.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                        found = 1;
                        if (card.getStatus().equals("frozen")) {
                            outputNode.put("status", "frozen");
                        } else if (acc.getBalance() <= acc.getMinimumBalance()) {
                            card.setStatus("frozen");
                            outputNode.put("status", "frozen");
                            Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                                    "You have reached the minimum" +
                                            " amount of funds, the card will be frozen")
                                    .build();
                            user.getTransactionHistory().addTransaction(transaction);

                        } else if (acc.getBalance()
                                - acc.getMinimumBalance() <= Constants.TREIZECI) {
                            outputNode.put("status", "warning");
                        }
                    }
                }
            }
        }
        //cardul n a fost gasit
        if (found == 0) {
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Card not found");
            node.set("output", outputNode);
            node.put("timestamp", timestamp);
            output.add(node);
        }
    }
}
