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





}
