package org.poo.banking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommerciantInput;
import org.poo.transactions.Transaction;
import org.poo.utils.Utils;

import java.util.List;

public class PayOnline {
    /**
     *
     * @param mapper
     * @param output
     * @param users
     * @param email
     * @param cardNumber
     * @param amount
     * @param currency
     * @param commerciantName
     * @param timestamp
     * @param commerciants
     */
    public void handlePayOnline(final ObjectMapper mapper, final ArrayNode output,
                                final List<User> users,
                                final String email, final String cardNumber,
                                final double amount,
                                final String currency,
                                final String commerciantName,
                                final int timestamp,
                                final List<CommerciantInput> commerciants) {
        if (amount <= 0)
            return;

        Card found = null;
        User cUser = null;
        Account cAcc = null;
        CommerciantInput commerciant = null;

        for (User user : users) {
            if (user.getEmail().equals(email)) {
                cUser = user;
                for (Account acc : user.getAccounts()) {
                    for (Card card : acc.getCards()) {
                        if (card.getCardNumber().equals(cardNumber)) {
                            cAcc = acc;
                            found = card;
                        }
                    }
                }
            }
        }
        //verificam daca cardul este inghetat
        if (found != null && "frozen".equals(found.getStatus())) {
            Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                    "The card is frozen")
                    .build();
            cUser.getTransactionHistory().addTransaction(transaction);
            return;
        }

        //card invalid
        if (cAcc == null || found == null) {
            ObjectNode responseNode = mapper.createObjectNode();
            responseNode.put("command", "payOnline");
            ObjectNode outputNode = mapper.createObjectNode();
            outputNode.put("timestamp", timestamp);
            outputNode.put("description", "Card not found");
            responseNode.set("output", outputNode);
            responseNode.put("timestamp", timestamp);
            output.add(responseNode);
            return;
        }
        //cautare comerciant
        for (CommerciantInput c : commerciants) {
            if (c.getCommerciant().equals(commerciantName)) {
                commerciant = c;
                break;
            }
        }
        //convertire suma
        double convert;
        if (currency.equals(cAcc.getCurrency())) {
            convert = amount;
        } else {
            convert = Converter.getInstance().convert(currency, cAcc.getCurrency(), amount);
        }
        //calcul comision si suma totala
        double fee = FeeCalculate.calculateFee(cUser, convert);
        double total = convert + fee;

        if (cAcc.getBalance() < total) {
            Transaction transaction = new Transaction.TransactionBuilder(timestamp,
                    "Insufficient funds").build();
            cUser.getTransactionHistory().addTransaction(transaction);
            return;
        }
        //convertire in RON pentru cashback
        double amountRON;
        if (currency.equals("RON")) {
            amountRON = amount;
        } else {
            amountRON = Converter.getInstance().convert(currency, "RON", amount);
        }
        //adaugare suma
        if (commerciant != null) {
            cAcc.addSpending(amountRON);
        }
        //calcul cashback
        double cashback = 0.0;
        if (commerciant != null) {
            cashback = Cashback.calculateCashback(cAcc, cUser, commerciant.getType(),
                    commerciant.getCashbackStrategy(), amountRON);
        }
        //actualizare balanta
        double balance = cAcc.getBalance() - total;
        if (cashback > 0) {
            double convertedCashback = currency.equals(cAcc.getCurrency()) ?
                    cashback : Converter.getInstance().convert("RON", cAcc.getCurrency(), cashback);
            balance += convertedCashback;
        }
        cAcc.setBalance(balance);
        //inregistrare tranzactie
        Transaction transaction = new Transaction.TransactionBuilder(timestamp, "Card payment")
                .builderAmount(String.valueOf(convert))
                .builderCommerciant(commerciantName)
                .build();
        cUser.getTransactionHistory().addTransaction(transaction);
        //verificare daca cardul este de tip one_time
        if (found.getType().equals("one_time")) {
            cAcc.remove(cardNumber);
            Transaction transaction1 = new Transaction.TransactionBuilder(timestamp,
                    "The card has been destroyed")
                    .builderCard(cardNumber)
                    .builderCardHolder(email)
                    .builderAccount(cAcc.getIBAN())
                    .build();
            cUser.getTransactionHistory().addTransaction(transaction1);

            String newCardNumber = Utils.generateCardNumber();
            Card newCard = new Card(newCardNumber, "one_time");
            cAcc.addCard(newCard);

            Transaction transaction2 = new Transaction.TransactionBuilder(timestamp,
                    "New card created")
                    .builderCard(newCardNumber)
                    .builderCardHolder(email)
                    .builderAccount(cAcc.getIBAN())
                    .build();
            cUser.getTransactionHistory().addTransaction(transaction2);
        }
    }
}
