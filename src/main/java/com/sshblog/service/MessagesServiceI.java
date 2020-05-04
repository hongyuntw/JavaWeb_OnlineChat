package com.sshblog.service;

import com.sshblog.entity.Messages;

import java.util.List;

public interface MessagesServiceI {
    public void saveMessage(Messages message);
    public List<Messages> findAllMessages(int id, int id2);
}
