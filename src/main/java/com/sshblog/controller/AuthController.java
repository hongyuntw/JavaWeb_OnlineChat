package com.sshblog.controller;


import com.sshblog.entity.Messages;
import com.sshblog.entity.Users;
import com.sshblog.model.Login;
import com.sshblog.service.MessagesServiceI;
import com.sshblog.service.UsersServiceI;
import com.sshblog.websocket.ChatWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class AuthController {
    @Autowired
    UsersServiceI usersServiceI;
    public void setUsersServiceI(UsersServiceI usersServiceI){
        this.usersServiceI = usersServiceI;
    }


    @Autowired
    MessagesServiceI messagesServiceI;
    public void setMessagesServiceI(MessagesServiceI messagesServiceI){
        this.messagesServiceI = messagesServiceI;
    }


//    judge user status
    @RequestMapping(value="/auth",method=RequestMethod.GET)
    public String Auth(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr){
        HttpSession session = request.getSession();

        if(session.getAttribute("user") == null){
            return "redirect:/login";
        }
        else{
            return "redirect:/index";
        }
    }

    @RequestMapping(value="/login",method=RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request,HttpServletResponse response){
        ModelAndView mav = new ModelAndView("auth/login");
        mav.addObject("login",new Login());
        return mav;
    }

    @RequestMapping(value="loginProcess",method=RequestMethod.POST)
    public ModelAndView loginProcess(HttpServletResponse response,HttpServletRequest request,
                                     @ModelAttribute("login") Login login){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        ModelAndView mav = new ModelAndView();
        String error_msg = "";
//        try to login
        if(this.usersServiceI.findByEmail(login.getEmail()).isEmpty()){
            error_msg += "cant find account <br>";
            mav.setViewName("auth/login");
            mav.addObject("error_msg",error_msg);
            return mav;
        }
        String encodedPassword = passwordEncoder.encode(login.getPassword());

        if(!this.usersServiceI.findByEmail(login.getEmail()).get(0).getPassword().equals(encodedPassword)){
            error_msg += "password does not match email <br>";
            mav.setViewName("auth/login");
            mav.addObject("error_msg",error_msg);
            return mav;
        }

        Users user = this.usersServiceI.findByEmail(login.getEmail()).get(0);
        request.getSession().setAttribute("user",user);

        mav.setViewName("redirect:auth");
        return mav;
    }

    @RequestMapping(value="/logout",method =RequestMethod.GET)
    public ModelAndView logout(HttpServletResponse response,HttpServletRequest request){
        ModelAndView mav = new ModelAndView("redirect:auth");

        HttpSession session = request.getSession();
        Users loginUser= (Users) session.getAttribute("user");
        session.removeAttribute("user");
        session.invalidate();

        Set<Map.Entry<String, WebSocketSession>> entrySet = ChatWebSocketHandler.USER_SOCKETSESSION_MAP.entrySet();
        //并查找出在线用户的WebSocketSession（会话），将其移除（不再对其发消息了。。）
        for (Map.Entry<String, WebSocketSession> entry : entrySet) {
            if(entry.getKey().equals(String.valueOf(loginUser.getId()))){
                ChatWebSocketHandler.USER_SOCKETSESSION_MAP.remove(entry.getKey());
                System.out.println("用戶登出，Socket移除:用戶ID" + entry.getKey());
                break;
            }
        }
        return mav;
    }



    @RequestMapping(value="/register" ,method=RequestMethod.GET)
    public ModelAndView register(HttpServletRequest request,HttpServletResponse response){
        ModelAndView mav = new ModelAndView("auth/register");
        mav.addObject("user",new Users());
        return mav;
    }


    @RequestMapping(value="/registerProcess",method=RequestMethod.POST)
    public ModelAndView registerProcess(HttpServletRequest request,HttpServletResponse response,
                                        @ModelAttribute("user") Users user){
        ModelAndView mav = new ModelAndView();
        String error_msg = "";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


        //        validate
        if(!(this.usersServiceI.findByEmail(user.getEmail()).isEmpty())){
            mav.setViewName("auth/register");
            error_msg += "email already been used <br>";
        }
//        password and password confirm not equal
        if(!request.getParameter("password_confirm").equals(user.getPassword())){
            mav.setViewName("auth/register");
            error_msg += "password confirm does not match password <br>";
        }
        if(user.getName().isEmpty() || user.getName()== null){
            mav.setViewName("auth/register");
            error_msg += "name can not be empty <br>";
        }
        if(user.getNickname().isEmpty() || user.getNickname()== null){
            mav.setViewName("auth/register");
            error_msg += "nickname can not be empty <br>";
        }
        if(!error_msg.equals("")){
            mav.addObject("error_msg", error_msg);
            return mav;
        }
        mav.setViewName("redirect:login");
        Date date = new Date();
        user.setCreateDate(date);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        this.usersServiceI.saveUser(user);
        HttpSession session = request.getSession();
        session.setAttribute("user",user);
        return mav;
    }

    @RequestMapping(value="/index",method=RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request,HttpServletResponse response){
        ModelAndView mav = new ModelAndView("index");
        Users loginUser = (Users) request.getSession().getAttribute("user");
        List<Users> allUsers = this.usersServiceI.findAllUsers();
        mav.addObject("allUsers",allUsers);
        Map<String,List<Messages>> usersMessages = new HashMap<String,List<Messages>>();
        for (Users allUser : allUsers) {
            if(allUser.getId() != loginUser.getId()){
                List<Messages> allMessages = this.messagesServiceI.findAllMessages(loginUser.getId(), allUser.getId());
                usersMessages.put(String.valueOf(allUser.getId()), allMessages);
            }
        }
//        mav.addObject("currentReceiveUser",new Users ());
        mav.addObject("allMessages",usersMessages);
        return mav;
    }


    @RequestMapping(value="/test/{id}",method= RequestMethod.GET)
    public ModelAndView Test(HttpServletRequest request, HttpServletResponse response,@PathVariable int id){
        System.out.print(id);
        ModelAndView mav = new ModelAndView("test");
        Users user = usersServiceI.findById(id);
        mav.addObject("user",user);
        return mav;
    }

}
