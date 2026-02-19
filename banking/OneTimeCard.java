package org.poo.banking;

public class OneTimeCard extends Card {
    /**
     *
     * @param cardNumber
     */
    public OneTimeCard(final String cardNumber) {
        super(cardNumber, "one_time");
    }

    /**
     *
     * @return
     */
    @Override
    public String getType() {
        return "one_time";
    }

    /**
     *
     * @param type
     */
    @Override
    public void setType(final String type) {
        super.setType(type);
    }

    /**
     *
     * @return
     */
    @Override
    public String getStatus() {
        return super.getStatus();
    }

    /**
     *
     * @param status
     */
    @Override
    public void setStatus(final String status) {
        super.setStatus(status);
    }
}
