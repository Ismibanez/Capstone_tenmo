package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated()")
public class UserAccountController {

    UserDao userDao;
    AccountDao accountDao;
    public UserAccountController(UserDao userDao, AccountDao accountDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/balance", method =  RequestMethod.GET)
    public Double getBalance(Principal principal){
        String name = principal.getName();
        User user = userDao.findByUsername(name);
        return accountDao.findByUserID(user.getId()).getBalance();
    }




//    public BigDecimal getBalance(Principal principal){
//        //principal.getName()
//        return userDao.getAccountBalance(au);
//    }
}
