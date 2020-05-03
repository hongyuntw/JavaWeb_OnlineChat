package com.sshblog.dao.impl;

import com.sshblog.dao.ContactsDAOI;
import com.sshblog.entity.Contacts;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ContactsDAOImpl implements ContactsDAOI {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Contacts> findAllContacts() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Contacts.class);
        return criteria.list();
    }

    @Override
    public void saveContact(Contacts contacts) {
        sessionFactory.getCurrentSession().save(contacts);
    }

    @Override
    public Contacts findById(int id) {
        Contacts contacts = (Contacts) sessionFactory.getCurrentSession().get(Contacts.class, id);
        return contacts;
    }

    @Override
    public void deleteContact(int id) {
        Contacts contacts = findById(id);
        sessionFactory.getCurrentSession().delete(contacts);
    }

    @Override
    public void updateContact(Contacts contacts) {
        sessionFactory.getCurrentSession().update(contacts);
    }

    @Override
    public List<Contacts> findByContactName(String name) {
        String hql = "from Contacts c where c.name like '%" + name + "%'";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }
}
