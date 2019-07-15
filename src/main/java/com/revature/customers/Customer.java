package com.revature.customers;

import java.math.BigDecimal;

public class Customer {

    // customer account tools
    // constructors, getters & setters, tostring, hashcode, equals
    private String title = "Customer";
    private int accountnumber;
    private String username;
    private String pw;
    private String firstname;
    private String middlename;
    private String lastname;
    private BigDecimal balance;

    public Customer() {
        super();
    }

    public Customer(String title, String username, String pw, String firstname, String middlename, String lastname,
            BigDecimal balance) {
        this.title = title;
        this.username = username;
        this.pw = pw;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.balance = balance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(int accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Customer Account Number: " + accountnumber + "\nBalance: " + balance + "\nFirst Name: " + firstname
                + "\nLast Name: " + lastname + "\nMiddlename: " + middlename + "\nUser Name: " + username;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + accountnumber;
        result = prime * result + ((balance == null) ? 0 : balance.hashCode());
        result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
        result = prime * result + ((lastname == null) ? 0 : lastname.hashCode());
        result = prime * result + ((middlename == null) ? 0 : middlename.hashCode());
        result = prime * result + ((pw == null) ? 0 : pw.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Customer other = (Customer) obj;
        if (accountnumber != other.accountnumber)
            return false;
        if (balance == null) {
            if (other.balance != null)
                return false;
        } else if (!balance.equals(other.balance))
            return false;
        if (firstname == null) {
            if (other.firstname != null)
                return false;
        } else if (!firstname.equals(other.firstname))
            return false;
        if (lastname == null) {
            if (other.lastname != null)
                return false;
        } else if (!lastname.equals(other.lastname))
            return false;
        if (middlename == null) {
            if (other.middlename != null)
                return false;
        } else if (!middlename.equals(other.middlename))
            return false;
        if (pw == null) {
            if (other.pw != null)
                return false;
        } else if (!pw.equals(other.pw))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}
