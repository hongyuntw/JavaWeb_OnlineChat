package com.sshblog.service;

import com.sshblog.entity.Contacts;

import java.util.List;

public interface ContactsServiceI {

    public List<Contacts> findAllUsers();

    public void saveContact(Contacts contacts);

    public void deleteContact(int id);

    public Contacts findById(int id);

    public void updateContact(Contacts contacts);

    public List<Contacts> findByUserName(String name);

}
