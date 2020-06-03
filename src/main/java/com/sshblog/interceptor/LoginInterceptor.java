package com.sshblog.interceptor;


import com.sshblog.entity.Users;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //獲取請求的RUi:去除http:localhost:8080這部分剩下的
        String uri = request.getRequestURI();

        if(uri.equals("/") || uri.equals("")){
            request.getRequestDispatcher("/auth").forward(request, response);
            return false;
        }
        //UTL:除了login.jsp是可以公開訪問的，其他的URL都進行攔截控制
        if (uri.indexOf("auth") >= 0 || uri.indexOf(".js") >= 0 || uri.indexOf(".css") >= 0 || uri.indexOf("ajax") >= 0) {
            return true;
        }
        if (uri.indexOf("login") >= 0 || uri.indexOf("register") >= 0 || uri.indexOf("active") >= 0 || uri.indexOf("auth/resetPassword") >= 0) {
            return true;
        }
        if(uri.indexOf("resetPasswordProcess")>=0){
            return true;
        }


        //獲取session
        HttpSession session = request.getSession();
//        Users user = (User) session.getAttribute("USER_SESSION");
//        //判斷session中是否有用戶數據，如果有，則返回true，繼續向下執行
//        if (user != null) {
//            return true;
//        }
        if (session.getAttribute("user") != null) {
            return true;
        }
        //不符合條件的給出提示信息，並轉發到登錄頁面
//        request.setAttribute("msg", "您還沒有登錄，請先登錄！");
        request.getRequestDispatcher("/auth").forward(request, response);

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}