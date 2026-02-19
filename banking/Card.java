package org.poo.banking;

//clasa ce reprezinta cardul
public class Card {
    private String cardNumber;
    private String status;
    private String type;

    /**
     *
     * @param cardNumber
     */
    public Card(final String cardNumber) {
        this.cardNumber = cardNumber;
        this.status = "active";
        this.type = "normal";
    }

    /**
     *
     * @param cardNumber
     * @param type
     */
    public Card(final String cardNumber, final String type) {
        this.cardNumber = cardNumber;
        this.status = "active";
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getCardNumber() {
        return cardNumber;
    }

    /**
     *
     * @return
     */
    public String getStatus() {
        return status;
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
     * @param cardNumber
     */
    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     *
     * @param status
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     *
     * @param type
     */
    public void setType(final String type) {
        this.type = type;
    }
}
