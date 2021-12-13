package com.techelevator.tenmo.model;

import java.io.Serializable;

public class Transfer {
    long id;
    Type typeId;

    public long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(long accountTo) {
        this.accountTo = accountTo;
    }

    Status statusId;
    long accountFrom;
    long accountTo;
    long userIdFrom;
    long userIdTo;
    Double amount;

    public Transfer(){}

    public enum Status{
        Pending(1), Approved(2), Rejected(3);

        int value;

        Status(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    public enum Type implements Serializable {
        Send(2), Request(1);

        int value;

        Type(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public Status getStatusId() {
        return statusId;
    }

    public void setStatusId(Status statusId) {
        this.statusId = statusId;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getTypeId() {
        return typeId;
    }

    public void setTypeId(Type typeId) {
        this.typeId = typeId;
    }

    public Long getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(Long userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public Long getUserIdTo() {
        return userIdTo;
    }

    public void setUserIdTo(Long userIdTo) {
        this.userIdTo = userIdTo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

