package org.poo.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.List;

public final class DeleteAccount {
    /**
     *
     * @param mapper
     * @param output
     * @param users
     * @param email
     * @param iban
     * @param timestamp
     */
    public void handleDeleteAccount(final ObjectMapper mapper,
                                    final ArrayNode output,
                                    final List<User> users,
                                    final String email,
                                    final String iban, final int timestamp) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "deleteAccount");

        ObjectNode outputNode = mapper.createObjectNode();

        //verificam daca contul exista
        int delete = 0;
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                for (Account acc : user.getAccounts()) {
                    if (acc.getIBAN().equals(iban)) {
                        if (acc.getBalance() == 0) {
                            user.getAccounts().remove(acc);
                            outputNode.put("success", "Account deleted");
                            delete = 1;
                        }
                        break;
                    }
                }
            }
        }
        //daca nu, afisam eroare
        if (delete == 0) {
            outputNode.put("error",
                    "Account couldn't be deleted - "
                           + "see org.poo.transactions for details");
        }
        outputNode.put("timestamp", timestamp);
        node.set("output", outputNode);
        node.put("timestamp", timestamp);
        output.add(node);
    }
}
