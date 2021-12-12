package com.techelevator.tenmo.model;

public class Account {
    Long id;
    Long userID;
    double balance;

    public Account() { }

    public Account(Long id, Long userID, double balance) {
        this.id = id;
        this.userID = userID;
        this.balance = balance;
    }

    public void receive(Account sender, double amount){
        //sender
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
