package org.poo.banking;

import java.util.*;

//clasa ce reprezinta contul bancar
public class Account {
    private String IBAN;
    private double balance;
    private String currency;
    private String type;
    private List<Card> cards;
    private double minBalance;
    //hashmap pentru a numara tranzactiile de tip Food, Clothes, etc
    private Map<String, Integer> counter;
    //hashmap pentru a vedea daca s a facut caashback
    private Map<String, Boolean> cashback;
    private double total;

    /**
     *
     * @param iban
     * @param currency
     * @param type
     */
    public Account(final String iban,
                   final String currency, final String type) {
        this.IBAN = iban;
        this.balance = 0.0;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        this.minBalance = 0.0;
        this.counter = new HashMap<>();
        this.counter.put("Food", 0);
        this.counter.put("Clothes", 0);
        this.counter.put("Tech", 0);
        this.total = 0.0;
        this.cashback = new HashMap<>();
        this.cashback.put("Food", false);
        this.cashback.put("Clothes", false);
        this.cashback.put("Tech", false);
    }

    /**
     *
     * @return
     */
    public String getIBAN() {
        return IBAN;
    }

    /**
     *
     * @return
     */
    public double getBalance() {
        return balance;
    }

    /**
     *
     * @return
     */
    public String getCurrency() {
        return currency;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     *
     * @param iban
     */
    public void setIBAN(final String iban) {
        this.IBAN = iban;
    }

    /**
     *
     * @param balance
     */
    public void setBalance(final double balance) {
        this.balance = balance;
    }

    /**
     *
     * @param currency
     */
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    /**
     *
     * @param type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     *
     * @param cards
     */
    public void setCards(final List<Card> cards) {
        this.cards = cards;
    }

    /**
     *
     * @param card
     */
    public void addCard(final Card card) {
        cards.add(card);
    }

    /**
     *
     * @param amount
     */
    public void setMinimumBalance(final double amount) {
        this.minBalance = amount;
    }

    /**
     *
     * @return
     */
    public double getMinimumBalance() {
        return minBalance;
    }

    /**
     *
     * @param cardNumber
     */
    public void remove(final String cardNumber) { //stergere numar card
        Iterator<Card> iterator = cards.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();
            if (card.getCardNumber().equals(cardNumber)) {
                iterator.remove();
            }
        }
    }

    /**
     *
     * @param cardNumber
     * @return
     */
    public Card find(final String cardNumber) {
        for (Card card : cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }

    /**
     *
     * @param type
     * @return
     */
    public int getCount(final String type) {
        if (counter.containsKey(type)) {
            return counter.get(type);
        }
        return 0;
    }

    /**
     *
     * @return
     */
    public double getTotal() {
        return total;
    }

    /**
     *
     * @param amount
     */
    public void addSpending(final double amount) {
        this.total += amount;
    }

    /**
     *
     * @param type
     * @return
     */
    public boolean cashbackUsed(final String type) {
        if (cashback.containsKey(type)) {
            return cashback.get(type);
        }
        return false;
    }

    /**
     *
     * @param type
     */
    public void markTrue(final String type) {
        if (type != null) {
            cashback.put(type, true);
        }
    }
}
