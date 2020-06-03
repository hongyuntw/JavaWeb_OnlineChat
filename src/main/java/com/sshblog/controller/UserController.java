package com.sshblog.controller;


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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    UsersServiceI usersServiceI;
    public void setUsersServiceI(UsersServiceI usersServiceI) {
        this.usersServiceI = usersServiceI;
    }


    @Autowired
    MessagesServiceI messagesServiceI;
    public void setMessagesServiceI(MessagesServiceI messagesServiceI) {
        this.messagesServiceI = messagesServiceI;
    }



    @RequestMapping(value="/edit",method = RequestMethod.GET)
    public ModelAndView EditUser(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("auth/edit");
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        mav.addObject("user", user);
        return mav;
    }

    @RequestMapping(value="/editProcess",method = RequestMethod.POST)
    public ModelAndView edirProcess(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file", required = false) MultipartFile file,
                                        @ModelAttribute("user") Users user) throws Exception, IOException {
        ModelAndView mav = new ModelAndView();
        String error_msg = "";

        HttpSession session = request.getSession();
        Users loginUser = (Users) session.getAttribute("user");


        if (user.getNickname().isEmpty() || user.getNickname() == null) {
            mav.setViewName("auth/edit");
            mav.addObject("user", loginUser);
            error_msg += "nickname can not be empty <br>";
            mav.addObject("error_msg", error_msg);
            return mav;
        }
        else{
            loginUser.setNickname(user.getNickname());
        }
        //        deal with image
        String filename = file.getOriginalFilename();
        if (filename.equals("") || filename.isEmpty()) {
            error_msg += "";
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
                mav.setViewName("auth/edit");
                mav.addObject("error_msg", error_msg);
                mav.addObject("user", loginUser);
                return mav;
            }
            else {
                //以絕對路徑儲存重名命後的圖片
                String filePath = request.getSession().getServletContext().getRealPath("/upload");
                System.out.println(filePath);
                file.transferTo(new File(filePath + "/" + newName + "." + ext));
                //把圖片儲存路徑儲存到資料庫
//                user.setImg("upload/"+newName + "." + ext);
                loginUser.setImg("upload/"+newName + "." + ext);
            }

        }


//        mav.setViewName("redirect:login");
        mav.setViewName("auth/edit");
        mav.addObject("user", loginUser);
        mav.addObject("error_msg", "update Success! <br>");


        this.usersServiceI.updateUser(loginUser);
        return mav;
    }




    @RequestMapping(value="/index/ajax/getOnlineUserNums", method = RequestMethod.GET)
    @ResponseBody
    public Integer getOnlineUserNums(){
        return ChatWebSocketHandler.USER_SOCKETSESSION_MAP.size();
    }


    @RequestMapping(value="resetPassword",method = RequestMethod.GET)
    public ModelAndView resetPwd(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = new ModelAndView();
        HttpSession session = request.getSession();
        Users loginUser = (Users) session.getAttribute("user");
        String resetPwdCode= UUID.randomUUID().toString().replaceAll("-", "");
        new Thread(new MailUtils(loginUser.getEmail(),resetPwdCode,false)).start();
        loginUser.setResetPwdCode(resetPwdCode);
        this.usersServiceI.updateUser(loginUser);


        mav.setViewName("auth/edit");
        mav.addObject("user", loginUser);
        mav.addObject("error_msg", "Already sent an reset password mail to you! <br>");
        return mav;
    }



    @RequestMapping(value="/resetPasswordProcess",method = RequestMethod.GET)
    public ModelAndView resetPwdProcess(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("auth/resetPassword");
        mav.addObject("code", request.getParameter("code"));
        return mav;
    }

    @RequestMapping(value="/resetPasswordProcess_Check",method = RequestMethod.POST)
    public ModelAndView resetPwdProcessCheck(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = new ModelAndView();
        String resetPwdCode = request.getParameter("code");
        mav.setViewName("auth/resetPassword");
        mav.addObject("code", request.getParameter("code"));
        String error_msg = "";

        if(this.usersServiceI.findByResetPwdCode(resetPwdCode).isEmpty()){
            mav.addObject("error_msg", "TOKEN invalid! <br>");
            return mav;
        }

        Users user = this.usersServiceI.findByResetPwdCode(resetPwdCode).get(0);
        String oldPwd = request.getParameter("oldpassword");
        String newPwd = request.getParameter("newpassword");
        String newPwdConfirm = request.getParameter("newpassword_confirm");

        String hashOldPwd = org.apache.commons.codec.digest.DigestUtils.sha256Hex(oldPwd);

        if(!hashOldPwd.equals(user.getPassword())){
            mav.addObject("error_msg", "Wrong Old Password! <br>");
            return mav;
        }

        if(!newPwd.equals(newPwdConfirm)){
            mav.addObject("error_msg", "new password should same as confirm! <br>");
            return mav;
        }

        String hashNewPwd  = org.apache.commons.codec.digest.DigestUtils.sha256Hex(newPwd);
        user.setPassword(hashNewPwd);
        this.usersServiceI.updateUser(user);


        mav.addObject("error_msg", "update password success! <br>");
        return mav;
    }


}
