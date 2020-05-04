package com.sshblog.service.impl;


import com.sshblog.dao.MessagesDAOI;
import com.sshblog.dao.UsersDAOI;
import com.sshblog.entity.Messages;
import com.sshblog.service.MessagesServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("messagesService")
@Transactional
public class MessagesServiceImpl implements MessagesServiceI {
    @Autowired
    private MessagesDAOI messagesDAOI;

    public void setUsersDAOI(MessagesDAOI messagesDAOI) {
        this.messagesDAOI = messagesDAOI;
    }


    @Override
    public void saveMessage(Messages message) {
        this.messagesDAOI.saveMessage(message);

    }

    @Override
    public List<Messages> findAllMessages(int id, int id2) {
        return this.messagesDAOI.findAllMessages(id,id2);
    }
}
