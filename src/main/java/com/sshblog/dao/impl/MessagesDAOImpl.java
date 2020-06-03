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
//        String hql="from Messages m where ( m.senderId = " + id + " AND m.receiverId =" + id2 +") OR ( m.senderId ="
//                + id2 + " AND m.receiverId =" + id + ") order by m.sendTime asc"; //升序
////        System.out.println(hql);
        String hql="from Messages m where ( m.senderId = :id AND m.receiverId = :id2 ) OR ( m.senderId = :id2 AND m.receiverId = :id) order by m.sendTime asc"; //升序
        Query q = sessionFactory.getCurrentSession().createQuery(hql);
        q.setParameter("id", id);
        q.setParameter("id2",id2);
        return q.list();
//        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }
}
