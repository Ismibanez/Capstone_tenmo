package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDAO implements TransferDAO {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Transfer createRequest(Transfer transfer){


        String sql = "INSERT INTO transfers (transfer_id,transfer_type_id,transfer_status_id, account_from, account_to, amount) " +
                "VALUES (DEFAULT, ?, ?, ?, ?, ?)";
         jdbcTemplate.update(sql,
                transfer.getTypeId().getValue(),
                transfer.getStatusId().getValue(),
                transfer.getAccountFrom(),
                transfer.getAccountTo(),
                transfer.getAmount());
        return transfer;
    }
    public Transfer[] getRequestByUser(User user){
        List<Transfer> transferList = new ArrayList<>();
        String sql = "Select transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "From transfers " +
                "Join accounts On transfers.account_to = accounts.account_id " +
                "Where accounts.user_id = ?";

        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql,user.getId());
        while (rows.next()){
            transferList.add(mapRowToTransfers(rows));
        }
        return transferList.toArray(new Transfer[0]);
    }
    public Transfer[] getSentByUser(User user){
        List<Transfer> transferList = new ArrayList<>();
        String sql = "Select transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "From transfers " +
                "Join accounts On transfers.account_from = accounts.account_id " +
                "Where accounts.user_id = ?";

        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql,user.getId());
        while (rows.next()){
            transferList.add(mapRowToTransfers(rows));
        }
        return transferList.toArray(new Transfer[0]);
    }

    @Override
    public Transfer getTransfer(long id) {
        String sql = "Select transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "From transfers " +
                "Where transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql,id);
        if(result.next()){
            return mapRowToTransfers(result);
        }
        return null;
    }

    private Transfer mapRowToTransfers(SqlRowSet rs){
        Transfer transfer = new Transfer();
        transfer.setId(rs.getLong("transfer_id"));

        for (Transfer.Type type: Transfer.Type.values()) {
            if (type.getValue() == rs.getLong("transfer_type_id")){
                transfer.setTypeId(type);
            }
        }

        for (Transfer.Status status: Transfer.Status.values()) {
            if (status.getValue() == rs.getLong("transfer_status_id")){
                transfer.setStatusId(status);
            }
        }
        transfer.setAccountFrom(rs.getLong("account_from"));
        transfer.setAccountTo(rs.getLong("account_to"));
        transfer.setAmount(rs.getDouble("amount"));
        return transfer;
    }
}
