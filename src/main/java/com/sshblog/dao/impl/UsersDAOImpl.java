package com.sshblog.dao.impl;

import com.sshblog.dao.UsersDAOI;
import com.sshblog.entity.Contacts;
import com.sshblog.entity.Users;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class UsersDAOImpl implements UsersDAOI {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Users findById(int id) {
        Users user = (Users) sessionFactory.getCurrentSession().get(Users.class,id);
        return user;
    }

    @Override
    public List<Users> findByEmail(String email) {
        String hql = "from Users u where u.email = '" + email + "'";
        return sessionFactory.getCurrentSession().createQuery(hql).list();
    }

    @Override
    public void saveUser(Users user) {
        sessionFactory.getCurrentSession().save(user);
    }

    @Override
    public void updateUser(Users user) {
        sessionFactory.getCurrentSession().update(user);
    }

    @Override
    public List<Users> findAllUsers() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Users.class);
        return criteria.list();
    }
}
