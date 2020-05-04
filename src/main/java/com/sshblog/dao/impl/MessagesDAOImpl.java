package com.sshblog.dao.impl;

import com.sshblog.dao.MessagesDAOI;
import com.sshblog.entity.Messages;
import com.sshblog.entity.Users;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;



@Repository
@Transactional
public class MessagesDAOImpl implements MessagesDAOI {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveMessage(Messages message) {
        sessionFactory.getCurrentSession().save(message);
    }

    @Override
    public List<Messages> findAllMessages(int id, int id2) {
//        Session session = sessionFactory.getCurrentSession();
        String hql="from Messages m where ( m.sender_id = " + id + "AND m.receiver_id =" + id2 +") OR ( m.sender_id ="
                + id2 + "AND m.receiver_id =" + id2 + ") order by m.send_time asc"; //升序
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }
}
