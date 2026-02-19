package org.poo.transactions;

import java.util.List;

public final class Transaction {
    private final int timestamp;
    private final String description;
    private final String senderIBAN;
    private final String receiverIBAN;
    private final String amount;
    private final String transferType;
    private final String commerciant;
    private final String card;
    private final String cardHolder;
    private final String account;
    private final String currency;
    private final List<String> accounts;
    private final String newPlan;
    private final String iban;

    /**
     *
     * @param builder
     */

    private Transaction(final TransactionBuilder builder) {
        this.timestamp = builder.timestamp;
        this.description = builder.description;
        this.senderIBAN = builder.senderIBAN;
        this.receiverIBAN = builder.receiverIBAN;
        this.amount = builder.amount;
        this.transferType = builder.transferType;
        this.commerciant = builder.commerciant;
        this.card = builder.card;
        this.cardHolder = builder.cardHolder;
        this.account = builder.account;
        this.currency = builder.currency;
        this.accounts = builder.accounts;
        this.newPlan = builder.newPlan;
        this.iban = builder.iban;
    }

    public String getIban() {
        return iban;
    }

    public String getNewPlan() {
        return newPlan;
    }

    public String getCurrency() {
        return currency;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getSenderIBAN() {
        return senderIBAN;
    }

    public String getReceiverIBAN() {
        return receiverIBAN;
    }

    public String getAmount() {
        return amount;
    }

    public String getTransferType() {
        return transferType;
    }

    public String getCommerciant() {
        return commerciant;
    }

    public String getCard() {
        return card;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getAccount() {
        return account;
    }
    //builder pt construirea tranzactiilor
    public static final class TransactionBuilder {
        private final int timestamp;
        private final String description;
        private String senderIBAN;
        private String receiverIBAN;
        private String amount;
        private String transferType;
        private String commerciant;
        private String card;
        private String cardHolder;
        private String account;
        private String currency;
        private List<String> accounts;
        private String savingsAcc;
        private String classicAcc;
        private String newPlan;
        private String iban;

        /**
         *
         * @param timestamp
         * @param description
         */

        public TransactionBuilder(final int timestamp, final String description) {
            this.timestamp = timestamp;
            this.description = description;
        }

        /**
         *
         * @param newSenderIBAN
         * @return
         */

        public TransactionBuilder builderSender(final String newSenderIBAN) {
            this.senderIBAN = newSenderIBAN;
            return this;
        }

        /**
         *
         * @param newPlan
         * @return
         */

        public TransactionBuilder builderNewPlanType(final String newPlan) {
            this.newPlan = newPlan;
            return this;
        }

        /**
         *
         * @param newIBAN
         * @return
         */

        public TransactionBuilder builderAccountIBAN(final String newIBAN) {
            this.iban = newIBAN;
            return this;
        }

        /**
         *
         * @param newSavingsAcc
         * @return
         */

        public TransactionBuilder builderSavingsAccount(final String newSavingsAcc) {
            this.savingsAcc = newSavingsAcc;
            return this;
        }

        /**
         *
         * @param newClassicAcc
         * @return
         */

        public TransactionBuilder builderClassicAccount(final String newClassicAcc) {
            this.classicAcc = newClassicAcc;
            return this;
        }

        /**
         *
         * @param newReceiverIBAN
         * @return
         */

        public TransactionBuilder builderReceiver(final String newReceiverIBAN) {
            this.receiverIBAN = newReceiverIBAN;
            return this;
        }

        /**
         *
         * @param newAmount
         * @return
         */

        public TransactionBuilder builderAmount(final String newAmount) {
            this.amount = newAmount;
            return this;
        }

        /**
         *
         * @param newTransferType
         * @return
         */

        public TransactionBuilder builderTransfer(final String newTransferType) {
            this.transferType = newTransferType;
            return this;
        }

        /**
         *
         * @param newCommerciant
         * @return
         */

        public TransactionBuilder builderCommerciant(final String newCommerciant) {
            this.commerciant = newCommerciant;
            return this;
        }

        /**
         *
         * @param newCard
         * @return
         */
        public TransactionBuilder builderCard(final String newCard) {
            this.card = newCard;
            return this;
        }

        /**
         *
         * @param newCardHolder
         * @return
         */
        public TransactionBuilder builderCardHolder(final String newCardHolder) {
            this.cardHolder = newCardHolder;
            return this;
        }

        /**
         *
         * @param newAccount
         * @return
         */
        public TransactionBuilder builderAccount(final String newAccount) {
            this.account = newAccount;
            return this;
        }

        /**
         *
         * @param newCurrency
         * @return
         */
        public TransactionBuilder builderCurrency(final String newCurrency) {
            this.currency = newCurrency;
            return this;
        }

        /**
         *
         * @param newAccounts
         * @return
         */
        public TransactionBuilder builderAccounts(final List<String> newAccounts) {
            this.accounts = newAccounts;
            return this;
        }

        /**
         *
         * @return
         */
        public Transaction build() {
            return new Transaction(this);
        }
    }
}
