package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Account> findAll() {
        List<Account> accounts = new ArrayList<>();
        String sql = "Select account_id, user_id, balance From accounts";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql);

        while (result.next()){
            Account account = mapRowToAccount(result);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public Account findByUserID(Long userID) {
        Account account = new Account();
        String sql = "Select account_id, user_id, balance From accounts " +
                "Where user_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,userID);
        if(result.next()){
            return mapRowToAccount(result);
        }
        throw new UsernameNotFoundException("User ID " + userID + " was not found.");
    }

    public boolean transaction(Account account1,Account account2 ) {

        String sql = "BEGIN TRANSACTION; " +
                "Update accounts Set balance = ? Where account_id = ?; " +
                "Update accounts Set balance = ? Where account_id = ?; " +
                "Commit;";
        try {
            int result = jdbcTemplate.update(sql,account1.getBalance(),account1.getId(),account2.getBalance(),account2.getId());
            if(result == 2){
                return true;
            }else{
                return false;
            }
        } catch (DataAccessException e){
            System.err.println(e.getMessage());
        }
        return false;
    }


    private Account mapRowToAccount(SqlRowSet s ){
        Account account = new Account();
        account.setId(s.getLong("account_id"));
        account.setUserID(s.getLong("user_id"));
        account.setBalance(s.getDouble("balance"));
        return account;
    }
}
