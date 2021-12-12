package com.techelevator.tenmo.model;

import java.io.Serializable;

public class Transfer {
    Long id;
    Long typeId;
    Status statusId;
    Long accountFrom;
    Long accountTo;
    Double amount;

    public enum Status{
        Pending(1), Approved(2), Rejected(3);

        private final int value;

        Status(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    public enum Type implements Serializable {
        Send(2), Request(1);

        private final int value;

        Type(int value) {
            this.value = value;
        }

        public String getType() {
            return this.name();
        }

        public int getValue(Type type) {
            return value;
        }
    }


    public Transfer(){}
    public Transfer(long typeId, long accountFrom, long accountTo, double amount) {
        this.typeId = typeId;
        this.statusId = Status.Pending;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }
    public Transfer(Type type, long accountFrom, long accountTo, Double amount) {
        this.typeId = (long)type.getValue(type);
        this.statusId = Status.Pending;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getStatusId() {
        return (long)statusId.getValue();
    }
    public void setStatusId(Status status) {
        this.statusId = status;
    }

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

