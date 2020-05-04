package com.sshblog.websocket;

import com.sshblog.entity.Users;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class ChatHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession(false);
            //如果用户已经登录，允许聊天
            if(session.getAttribute("user") != null){
                //获取登录的用户
                Users loginUser= (Users) session.getAttribute("user") ;
                //将用户放入socket处理器的会话(WebSocketSession)中
                attributes.put("loginUser", loginUser);
                System.out.println("Websocket:用戶[ID:" + (loginUser.getId() + ",Name:"+loginUser.getNickname()+"]要建立連接"));
            }
            else{
                //用户没有登录，拒绝聊天
                //握手失败！
                System.out.println("--------------握手已失败...");
                return false;
            }
        }
        System.out.println("--------------握手開始...");
        return true;
    }

    /**
     * 握手之后
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        System.out.println("--------------握手成功啦...");
    }

}
