package org.poo.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class PrintUsers {
    /**
     *
     * @param mapper
     * @param output
     * @param users
     * @param timestamp
     */
    public void handlePrintUsers(final ObjectMapper mapper, final ArrayNode output,
                                 final List<User> users, final int timestamp) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "printUsers");

        ArrayNode uArray = mapper.createArrayNode();
        for (User user : users) {
            //afisare in formatul cerut
            ObjectNode uNode = mapper.createObjectNode();
            uNode.put("firstName", user.getFirstName());
            uNode.put("lastName", user.getLastName());
            uNode.put("email", user.getEmail());

            ArrayNode accArray = mapper.createArrayNode();
            for (Account acc : user.getAccounts()) {
                ObjectNode accNode = mapper.createObjectNode();
                accNode.put("IBAN", acc.getIBAN());
                accNode.put("balance", acc.getBalance());
                accNode.put("currency", acc.getCurrency());
                accNode.put("type", acc.getType());

                ArrayNode cArray = mapper.createArrayNode();
                for (Card card : acc.getCards()) {
                    ObjectNode cNode = mapper.createObjectNode();
                    cNode.put("cardNumber", card.getCardNumber());
                    cNode.put("status", card.getStatus());
                    cArray.add(cNode);
                }
                accNode.set("cards", cArray);
                accArray.add(accNode);
            }
            uNode.set("accounts", accArray);
            uArray.add(uNode);
        }
        node.set("output", uArray);
        node.put("timestamp", timestamp);
        output.add(node);
    }

}
