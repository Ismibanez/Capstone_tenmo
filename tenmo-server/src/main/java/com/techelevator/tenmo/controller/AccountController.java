package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/account")
public class AccountController {
    // create two separate controllers
    private final UserDao userDao;
    private final AccountDao accountDao;
    private final TransferDAO transferDAO;

    public AccountController(UserDao userDao, AccountDao accountDao, TransferDAO transferDAO) {
        this.accountDao = accountDao;
        this.userDao = userDao;
        this.transferDAO = transferDAO;
    }

    @RequestMapping(value = "/balance", method =  RequestMethod.GET)
    public Double getBalance(Principal principal){
        String name = principal.getName();
        User user = userDao.findByUsername(name);
        return accountDao.findByUserID(user.getId()).getBalance();
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @RequestMapping(value = "/transfer", method =  RequestMethod.POST)
    public Transfer requestTransfer(@Valid @RequestBody Transfer transfer){
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

    @RequestMapping(value = "/history", method =  RequestMethod.GET)
    public Transfer[] getAllTransfersByUser(Principal principal){
        User user = userDao.findByUsername(principal.getName());
        List<Transfer> list = new ArrayList<Transfer>(Arrays.asList(transferDAO.getRequestByUser(user)));
        list.addAll(Arrays.asList(transferDAO.getSentByUser(user)));
        return list.toArray(new Transfer[0]);
    }
    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id){
        return transferDAO.getTransfer(id);
    }
}
