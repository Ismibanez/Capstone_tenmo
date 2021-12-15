package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.exception.UserNotFoundException;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/transfer")
public class TransferController {
    private final AccountDao accountDao;
    private final TransferDAO transferDAO;

    public TransferController(AccountDao accountDao, TransferDAO transferDAO) {
        this.accountDao = accountDao;
        this.transferDAO = transferDAO;
    }


    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(method =  RequestMethod.POST)
    public Transfer sendBucksTransfer(@Valid @RequestBody Transfer transfer) {
        Account sender = accountDao.findByUserID(transfer.getUserIdFrom());
        Account receiver = accountDao.findByUserID(transfer.getUserIdTo());

        if(Transfer.Type.values()[transfer.getTypeId().ordinal()] == Transfer.Type.Send) {
            if (sender.sendBalance(receiver,transfer.getAmount())) {
                accountDao.transaction(sender, receiver);
                transfer.setStatusId(Transfer.Status.Approved);
            }else{
                transfer.setStatusId(Transfer.Status.Rejected);
            }
        }else {
            transfer.setStatusId(Transfer.Status.Pending);

        }

        transfer.setAccountFrom(sender.getId());
        transfer.setAccountTo(receiver.getId());
        return transferDAO.createRequest(transfer);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id){
        return transferDAO.getTransfer(id);
    }
}
