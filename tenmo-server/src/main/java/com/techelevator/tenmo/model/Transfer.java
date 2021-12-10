package com.techelevator.tenmo.model;

public class Transfer {
    Long id;
    Long typeId;
    Status statusId;
    Long accountFrom;
    Long accountTo;
    Double amount;
    enum Status{
        Pending, Approved, Rejected
    }



    public Transfer(Long accountFrom, Long accountTo, Double amount) {
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
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
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
