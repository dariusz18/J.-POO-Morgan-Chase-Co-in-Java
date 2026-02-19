package org.poo.banking;

public class SavingsAccount extends Account {
    private double interestRate;//rata dobanzii pentru contul de savings

    /**
     *
     * @param IBAN
     * @param currency
     */
    public SavingsAccount(final String IBAN, final String currency) {
        super(IBAN, currency, "savings");
        this.interestRate = 0.0;
    }

    /**
     *
     * @return
     */
    @Override
    public String getType() {
        return "savings";
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
    public double getInterestRate() {
        return interestRate;
    }

    /**
     *
     * @param interestRate
     */
    public void setInterestRate(final double interestRate) {
        this.interestRate = interestRate;
    }
}
