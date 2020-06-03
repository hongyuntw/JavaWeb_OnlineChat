package com.sshblog.controller;


import com.sshblog.entity.Messages;
import com.sshblog.entity.Users;
import com.sshblog.model.Login;
import com.sshblog.service.MessagesServiceI;
import com.sshblog.service.UsersServiceI;
import com.sshblog.util.MailUtils;
import com.sshblog.websocket.ChatWebSocketHandler;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
public class AuthController {
    @Autowired
    UsersServiceI usersServiceI;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public void setUsersServiceI(UsersServiceI usersServiceI) {
        this.usersServiceI = usersServiceI;
    }


    @Autowired
    MessagesServiceI messagesServiceI;

    public void setMessagesServiceI(MessagesServiceI messagesServiceI) {
        this.messagesServiceI = messagesServiceI;
    }


    //    judge user status
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String defaltURI(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr) {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        else {
            return "redirect:/index";
        }
    }
//
//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String _defaltURI(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr) {
//        HttpSession session = request.getSession();
//        if (session.getAttribute("user") == null) {
//            return "redirect:/login";
//        }
//        else {
//            return "redirect:/index";
//        }
//    }

    //    judge user status
    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public String Auth(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr) {
        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        else {
            return "redirect:/index";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("auth/login");
        mav.addObject("login", new Login());
        return mav;
    }

    @RequestMapping(value = "loginProcess", method = RequestMethod.POST)
    public ModelAndView loginProcess(HttpServletResponse response, HttpServletRequest request,
                                     @ModelAttribute("login") Login login) {



        ModelAndView mav = new ModelAndView();
        String error_msg = "";
//        try to login
        if (this.usersServiceI.findByEmail(login.getEmail()).isEmpty()) {
            error_msg += "cant find account <br>";
            mav.setViewName("auth/login");
            mav.addObject("error_msg", error_msg);
            return mav;
        }
        String encodedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(login.getPassword());
        Users user = this.usersServiceI.findByEmail(login.getEmail()).get(0);
        String userPassword = user.getPassword();


        if (!userPassword.equals(encodedPassword)) {
            error_msg += "password does not match email <br>";
            mav.setViewName("auth/login");
            mav.addObject("error_msg", error_msg);
            return mav;
        }


        if(user.getState()==0){
            error_msg += "Account has not been activated <br>";
            mav.setViewName("auth/login");
            mav.addObject("error_msg", error_msg);
            return mav;
        }
        request.getSession().setAttribute("user", user);
        mav.setViewName("redirect:auth");
        return mav;
    }

    @RequestMapping(value="/active",method = RequestMethod.GET)
    public ModelAndView active(HttpServletResponse response, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("auth/login");
        mav.addObject("login", new Login());

        String code = request.getParameter("code");

        if (!(this.usersServiceI.findByCode(code).isEmpty())) {
            mav.addObject("error_msg", "validation success <br> now U can login <br>");
            Users user = this.usersServiceI.findByCode(code).get(0);
            user.setState(1);
            this.usersServiceI.updateUser(user);
        }
        else{
            mav.addObject("error_msg", "validation failed <br>");
        }

        return mav;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletResponse response, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("redirect:auth");
        HttpSession session = request.getSession();
        Users loginUser = (Users) session.getAttribute("user");
        session.removeAttribute("user");
        session.invalidate();

        Set<Map.Entry<String, WebSocketSession>> entrySet = ChatWebSocketHandler.USER_SOCKETSESSION_MAP.entrySet();
        for (Map.Entry<String, WebSocketSession> entry : entrySet) {
            if (entry.getKey().equals(String.valueOf(loginUser.getId()))) {
                ChatWebSocketHandler.USER_SOCKETSESSION_MAP.remove(entry.getKey());
                System.out.println("用戶登出，Socket移除:用戶ID" + entry.getKey());
                break;
            }
        }
        return mav;
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView register(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("auth/register");
        mav.addObject("user", new Users());
        return mav;
    }


    @RequestMapping(value = "/registerProcess", method = RequestMethod.POST)
    public ModelAndView registerProcess(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file", required = false) MultipartFile file,
                                        @ModelAttribute("user") Users user) throws Exception, IOException {
        ModelAndView mav = new ModelAndView();
        String error_msg = "";
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        //        validate
        if (!(this.usersServiceI.findByEmail(user.getEmail()).isEmpty())) {
            mav.setViewName("auth/register");
            error_msg += "email already been used <br>";
        }
//        password and password confirm not equal
        if (!request.getParameter("password_confirm").equals(user.getPassword())) {
            mav.setViewName("auth/register");
            error_msg += "password confirm does not match password <br>";
        }
        if (user.getName().isEmpty() || user.getName() == null) {
            mav.setViewName("auth/register");
            error_msg += "name can not be empty <br>";
        }
        if (user.getNickname().isEmpty() || user.getNickname() == null) {
            mav.setViewName("auth/register");
            error_msg += "nickname can not be empty <br>";

        }
        //        deal with image


        String filename = file.getOriginalFilename();
        if (filename.equals("") || filename.isEmpty()) {
            error_msg += "You must upload image <br>";
            mav.setViewName("auth/register");
            mav.addObject("error_msg", error_msg);
            return mav;
        }
        else {
            String newName = UUID.randomUUID().toString().replaceAll("-", "");
            //獲取圖片名稱
            String imageName = file.getOriginalFilename();
            //獲得檔案型別（可以判斷如果不是圖片，禁止上傳）
            String contentType = file.getContentType();
            //獲得檔案字尾名
            //String suffixName=contentType.substring(contentType.indexOf("/")+1);
            //獲取檔案的副檔名
            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            ext = ext.toLowerCase();
            if (!(ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png") || ext.equals("bmp"))) {
                error_msg += "Pleas upload image file <br>";
                mav.setViewName("auth/register");
                mav.addObject("error_msg", error_msg);
                return mav;
            }
            else {
                //以絕對路徑儲存重名命後的圖片
                String filePath = request.getSession().getServletContext().getRealPath("/upload");
                System.out.println(filePath);
                file.transferTo(new File(filePath + "/" + newName + "." + ext));
                //把圖片儲存路徑儲存到資料庫
                user.setImg("upload/"+newName + "." + ext);
            }

        }

        if (!error_msg.equals("")) {
            mav.addObject("error_msg", error_msg);
            return mav;
        }

        String code= UUID.randomUUID().toString().replaceAll("-", "");

        new Thread(new MailUtils(user.getEmail(),code,true)).start();;
        user.setCode(code);




//        mav.setViewName("redirect:login");
        mav.setViewName("auth/login");
        mav.addObject("login", new Login());
        mav.addObject("error_msg", "already send a validation mail <br>");


        Date date = new Date();
        user.setCreateDate(date);
        user.setState(0);
        String encodedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(user.getPassword());

        user.setPassword(encodedPassword);
        this.usersServiceI.saveUser(user);
//        HttpSession session = request.getSession();
//        session.setAttribute("user", user);
        return mav;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("index");
        Users loginUser = (Users) request.getSession().getAttribute("user");
        List<Users> allUsers = this.usersServiceI.findAllUsers();
        mav.addObject("allUsers", allUsers);
        Map<String, List<Messages>> usersMessages = new HashMap<String, List<Messages>>();
        for (Users allUser : allUsers) {
            if (allUser.getId() != loginUser.getId()) {
                List<Messages> allMessages = this.messagesServiceI.findAllMessages(loginUser.getId(), allUser.getId());
                usersMessages.put(String.valueOf(allUser.getId()), allMessages);
            }
        }
//        mav.addObject("currentReceiveUser",new Users ());
        mav.addObject("allMessages", usersMessages);
        return mav;
    }


    @RequestMapping(value = "/test/{id}", method = RequestMethod.GET)
    public ModelAndView Test(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {
        System.out.print(id);
        ModelAndView mav = new ModelAndView("test");
        Users user = usersServiceI.findById(id);
        mav.addObject("user", user);
        return mav;
    }

}
