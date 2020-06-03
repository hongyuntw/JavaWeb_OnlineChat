package com.sshblog.dao;

import com.sshblog.entity.Contacts;
import com.sshblog.entity.Users;

import java.util.List;

public interface UsersDAOI {

    public Users findById(int id);
    public void saveUser(Users user);
    public void updateUser(Users user);
    public List<Users> findByEmail(String email);
    public List<Users> findAllUsers();

    public List<Users> findByCode(String code);
    public List<Users> findByResetPwdCode(String code);

}
