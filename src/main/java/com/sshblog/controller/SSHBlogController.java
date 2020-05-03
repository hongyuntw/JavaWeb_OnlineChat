package com.sshblog.controller;

import com.sshblog.entity.Contacts;
import com.sshblog.service.ContactsServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/asdad")
public class SSHBlogController {
    @Autowired
    private ContactsServiceI contactsServiceI;

    public void setContactsServiceI(ContactsServiceI contactsServiceI) {
        this.contactsServiceI = contactsServiceI;
    }

    @RequestMapping("index")
    public ModelAndView indexPage(){
        ModelAndView mav = new ModelAndView("index");
        List<Contacts> contacts = this.contactsServiceI.findAllUsers();
        mav.addObject("contacts",contacts);
        return mav;
    }

    @RequestMapping(value = "save", method = RequestMethod.GET)
    public ModelAndView newContactPage(){
        ModelAndView mav = new ModelAndView("newContact");
        return mav;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String newContact(@RequestParam("dateOfb")String dob, Contacts contacts) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dob);
        contacts.setDob(date);
        this.contactsServiceI.saveContact(contacts);
        return "redirect:index.html";
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String deleteContact(int id) {
        this.contactsServiceI.deleteContact(id);
        return "redirect:index.html";
    }

    @RequestMapping(value = "update", method = RequestMethod.GET)
    public ModelAndView editContactPage(int id){
        ModelAndView mav = new ModelAndView("editContact");
        Contacts contacts = this.contactsServiceI.findById(id);
        mav.addObject("editContact",contacts);
        return mav;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String editContact(@RequestParam("dateOfb")Date date, Contacts contacts){
        contacts.setDob(date);
        this.contactsServiceI.updateContact(contacts);
        return "redirect:index.html";
    }

    @RequestMapping(value = "search", method = RequestMethod.POST)
    public ModelAndView searchByContactName(String name){
        ModelAndView mav = new ModelAndView("index");
        List<Contacts> contacts = this.contactsServiceI.findByUserName(name);
        mav.addObject("contacts",contacts);
        return mav;
    }

    @RequestMapping(value = "findAllUsers", method = RequestMethod.GET)
    @ResponseBody
    public List<Contacts> findAllUsers(){
        return this.contactsServiceI.findAllUsers();
    }
}
