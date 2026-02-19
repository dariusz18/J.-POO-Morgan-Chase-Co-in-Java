package org.poo.banking;

import org.poo.transactions.TransactionHistory;

import java.util.ArrayList;
import java.util.List;

//clasa ce reprezinta user ul
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String birthDate;
    private String occupation;
    private List<Account> accounts;
    private TransactionHistory transactionHistory = new TransactionHistory();
    private String plan;

    /**
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param birthDate
     * @param occupation
     */
    public User(final String firstName, final String lastName, final String email,
                final String birthDate, final String occupation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDate = birthDate;
        this.occupation = occupation;
        this.accounts = new ArrayList<>();
        this.transactionHistory = new TransactionHistory();
        if (occupation.equals("student")) {
            this.plan = "student";
        }
        else {
            this.plan = "standard";
        }
    }

    //ma asigur ca nu se poate face downgrade

    /**
     *
     * @param newPlan
     * @return
     */
    public boolean upgrade(final String newPlan) {
        if (this.plan.equals("gold") && newPlan.equals("silver")) {
            return false;
        }
        if (this.plan.equals("gold") && newPlan.equals("student")) {
            return false;
        }
        if (this.plan.equals("gold") && newPlan.equals("standard")) {
            return false;
        }
        if (this.plan.equals("silver")
               && (newPlan.equals("standard") || newPlan.equals("student"))) {
            return false;
        }
        return true;
    }

    /**
     *
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     *
     * @return
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     *
     * @return
     */
    public List<Account> getAccounts() {
        return accounts;
    }

    /**
     *
     * @return
     */
    public String getPlan() {
        return plan;
    }

    /**
     *
     * @return
     */
    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }

    /**
     *
     * @param firstName
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @param email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     *
     * @param plan
     */
    public void setPlan(final String plan) {
        this.plan = plan;
    }

    /**
     *
     * @param accounts
     */
    public void setAccounts(final List<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     *
     * @param account
     */
    public void addAccount(final Account account) {
        accounts.add(account);
    }
}
