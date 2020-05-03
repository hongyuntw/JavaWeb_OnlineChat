package com.sshblog.service.impl;


import com.sshblog.dao.UsersDAOI;
import com.sshblog.entity.Users;
import com.sshblog.service.UsersServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("usersService")
@Transactional
public class UsersServiceImpl implements UsersServiceI {
    @Autowired
    private UsersDAOI usersDAOI;

    public void setUsersDAOI(UsersDAOI usersDAOI) {
        this.usersDAOI = usersDAOI;
    }

    @Override
    public Users findById(int id) {
        return this.usersDAOI.findById(id);
    }

    @Override
    public void saveUser(Users user) {
        this.usersDAOI.saveUser(user);
    }
    @Override
    public void updateUser(Users user) {
        this.usersDAOI.updateUser(user);
    }

    @Override
    public List<Users> findByEmail(String email) {
        return this.usersDAOI.findByEmail(email);
    }

    @Override
    public List<Users> findAllUsers() {
        return this.usersDAOI.findAllUsers();
    }

}
