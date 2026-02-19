package org.poo.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.transactions.Transaction;

import java.util.List;

public class SendMoney {
    /**
     *
     * @param objectMapper
     * @param output
     * @param users
     * @param fromAcc
     * @param toAcc
     * @param amount
     * @param email
     * @param description
     * @param timestamp
     */
    public void handleSendMoney(final ObjectMapper objectMapper, final ArrayNode output,
                                final List<User> users, final String fromAcc,
                                final String toAcc,
                                final double amount, final String email,
                                final String description,
                                final int timestamp) {

        Account receiverAcc = null; //cont receiver
        Account senderAcc = null; //cont sender
        User receiver = null; //user receiver
        User sender = null; //user sender
        double convert;

        //cautare cont && cont sender
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(fromAcc)) {
                    sender = user;
                    senderAcc = account;
                }
            }
        }


        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(toAcc)) {
                    receiverAcc = account;
                    receiver = user;
                }
            }
        }
        //verificam daca transferul poate fi facut
        if (senderAcc == null || receiverAcc == null || sender == null) {
            ObjectNode responseNode = objectMapper.createObjectNode();
            responseNode.put("command", "sendMoney");

            ObjectNode outputNode = objectMapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "User not found");

            responseNode.set("output", outputNode);
            responseNode.put("timestamp", timestamp);

            output.add(responseNode);
            return;
        }

        //calcul fee && suma totala
        double fee = FeeCalculate.calculateFee(sender, amount);
        double total = amount + fee;

        if (senderAcc.getBalance() < total) {
            Transaction failedTransaction = new Transaction.TransactionBuilder(timestamp,
                    "Insufficient funds").build();
            sender.getTransactionHistory().addTransaction(failedTransaction);
            return;
        }
        //convertire
        convert = amount;
        if (!senderAcc.getCurrency().equals(receiverAcc.getCurrency())) {
            convert = Converter.getInstance().convert(senderAcc.getCurrency(),
                    receiverAcc.getCurrency(), amount);
        }

        senderAcc.setBalance(senderAcc.getBalance() - total);
        receiverAcc.setBalance(receiverAcc.getBalance() + convert);

        //inregistrare tranzactii
        Transaction sentTransaction = new Transaction.TransactionBuilder(timestamp, description)
                .builderSender(senderAcc.getIBAN())
                .builderReceiver(receiverAcc.getIBAN())
                .builderAmount(amount + " " + senderAcc.getCurrency())
                .builderTransfer("sent")
                .build();
        sender.getTransactionHistory().addTransaction(sentTransaction);

        Transaction receivedTransaction = new Transaction.TransactionBuilder(timestamp, description)
                .builderSender(senderAcc.getIBAN())
                .builderReceiver(receiverAcc.getIBAN())
                .builderAmount(amount + " " + senderAcc.getCurrency())
                .builderTransfer("received")
                .build();
        receiver.getTransactionHistory().addTransaction(receivedTransaction);
    }
}
