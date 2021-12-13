package com.techelevator.tenmo.model;

import java.io.Serializable;

public class Transfer {
    long id;
    Type typeId;
    Status statusId;
    long userIdFrom;
    long userIdTo;
    long accountFrom;
    long accountTo;
    double amount;

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

    public Transfer(){
        this.statusId = Status.Pending;
    }
    public Transfer(long typeId, long accountFrom, long accountTo, double amount) {
        this.typeId = Type.values()[(int)typeId];
        this.statusId = Status.Pending;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }
    public Transfer(Type type, long accountFrom, long accountTo, Double amount) {
        this.typeId = type;
        this.statusId = Status.Pending;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public long getUserIdFrom() {
        return userIdFrom;
    }

    public void setUserIdFrom(long userIdFrom) {
        this.userIdFrom = userIdFrom;
    }

    public long getUserIdTo() {
        return userIdTo;
    }

    public void setUserIdTo(long userIdTo) {
        this.userIdTo = userIdTo;
    }

    public long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Type typeId) {
        this.typeId = typeId;
    }

    public Status getStatusId() {
        return this.statusId;
    }
    public void setStatusId(Status status) {
        this.statusId = status;
    }

    public long getAccountFrom() {
        return this.accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public long getAccountTo() {
        return this.accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
