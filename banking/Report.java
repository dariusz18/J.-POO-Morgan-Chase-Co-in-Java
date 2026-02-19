package org.poo.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.transactions.Transaction;

import java.util.List;

public final class Report {
    /**
     *
     * @param mapper
     * @param output
     * @param users
     * @param accountIban
     * @param startTime
     * @param endTime
     * @param timestamp
     */
    public void handleReport(final ObjectMapper mapper,
                             final ArrayNode output, final List<User> users,
                            final String accountIban, final int startTime,
                             final int endTime, final int timestamp) {
        ObjectNode node = mapper.createObjectNode();
        node.put("command", "report");
        ObjectNode report = mapper.createObjectNode();
        User user = null;
        Account account = null;
        ArrayNode transactions = mapper.createArrayNode();
        //cautam contul si utilizatorul
        for (User u : users) {
            for (Account acc : u.getAccounts()) {
                if (acc.getIBAN().equals(accountIban)) {
                    account = acc;
                    user = u;
                }
            }
        }
        //daca utilizatorul exista cream raportul
        if (user != null) {
            report.put("IBAN", account.getIBAN());
            report.put("balance", account.getBalance());
            report.put("currency", account.getCurrency());
            //adaugam tranzactiile in intervalul specificat
            for (Transaction transaction : user.getTransactionHistory().getTransactions()) {
                if (transaction.getTimestamp() >= startTime
                       && transaction.getTimestamp() <= endTime) {
                    ObjectNode tNode = create(mapper, transaction);
                    if (tNode != null) {
                        transactions.add(tNode);
                    }
                }
            }
            report.set("transactions", transactions);
        }

        node.set("output", report);
        node.put("timestamp", timestamp);
        output.add(node);
    }
    //functie ce creeaza un nod pt o tranzactie
    private ObjectNode create(final ObjectMapper mapper,
                              final Transaction transaction) {
        ObjectNode tNode = mapper.createObjectNode();
        tNode.put("timestamp", transaction.getTimestamp());
        tNode.put("description", transaction.getDescription());

        if (transaction.getSenderIBAN() != null) {
            tNode.put("senderIBAN", transaction.getSenderIBAN());
            tNode.put("receiverIBAN", transaction.getReceiverIBAN());
            tNode.put("amount", transaction.getAmount());
            tNode.put("transferType", transaction.getTransferType());
        }

        if (transaction.getCommerciant() != null) {
            addAmount(tNode, transaction.getAmount());
            tNode.put("commerciant", transaction.getCommerciant());
        }

        if (transaction.getCard() != null) {
            tNode.put("card", transaction.getCard());
            tNode.put("cardHolder", transaction.getCardHolder());
            tNode.put("account", transaction.getAccount());
        }

        return tNode;
    }

    private void addAmount(final ObjectNode node, final String amount) {
        String[] parts = amount.split(" ");
        double value = Double.valueOf(parts[0]);
        node.put("amount", value);
    }
}
