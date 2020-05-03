package com.sshblog.service.impl;

import com.sshblog.dao.ContactsDAOI;
import com.sshblog.entity.Contacts;
import com.sshblog.service.ContactsServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("contactsService")
@Transactional
public class ContactsServiceImpl implements ContactsServiceI {
    @Autowired
    private ContactsDAOI contactsDAOI;

    public void setContactsDAOI(ContactsDAOI contactsDAOI) {
        this.contactsDAOI = contactsDAOI;
    }

    @Override
    public List<Contacts> findAllUsers() {
        return this.contactsDAOI.findAllContacts();
    }

    @Override
    public void saveContact(Contacts contacts) {
        this.contactsDAOI.saveContact(contacts);
    }

    @Override
    public void deleteContact(int id) {
        this.contactsDAOI.deleteContact(id);
    }

    @Override
    public Contacts findById(int id) {
        return this.contactsDAOI.findById(id);
    }

    @Override
    public void updateContact(Contacts contacts) {
        this.contactsDAOI.updateContact(contacts);
    }

    @Override
    public List<Contacts> findByUserName(String name) {
        return this.contactsDAOI.findByContactName(name);
    }
}
