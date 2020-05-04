package com.sshblog.dao;

import com.sshblog.entity.Messages;
import com.sshblog.entity.Users;

import java.util.List;

public interface MessagesDAOI {

    public void saveMessage(Messages message);
    public List<Messages> findAllMessages(int id,int id2);
}
