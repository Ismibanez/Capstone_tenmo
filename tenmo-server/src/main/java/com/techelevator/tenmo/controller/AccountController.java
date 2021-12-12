package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/account", method =  RequestMethod.GET)
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
        return transferDAO.createRequest(transfer);
    }
}
