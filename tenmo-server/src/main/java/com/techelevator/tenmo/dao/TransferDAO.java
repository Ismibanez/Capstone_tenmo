package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

public interface TransferDAO {

    Transfer createRequest(Transfer transfer);
    Transfer[] getRequestByUser(User user);
    Transfer[] getSentByUser(User user);


}
