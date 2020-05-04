package com.sshblog.websocket;

import com.google.gson.Gson;
import com.sshblog.entity.Messages;
import com.sshblog.entity.Users;
//import com.sshblog.util.GsonUtils;
import com.sshblog.service.MessagesServiceI;
import com.sshblog.service.UsersServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.util.HtmlUtils;
import org.json.JSONObject;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component("chatWebSocketHandler")
public class ChatWebSocketHandler implements WebSocketHandler {

    @Autowired
    MessagesServiceI messagesServiceI;
    public void setMessagesServiceI(MessagesServiceI messagesServiceI){
        this.messagesServiceI = messagesServiceI;
    }


    //在线用户的SOCKET session (存储了所有的通信通道)
    public static final Map<String, WebSocketSession> USER_SOCKETSESSION_MAP;

    //存储所有的在线用户
    static {
        USER_SOCKETSESSION_MAP = new HashMap<String, WebSocketSession>();
    }

    /**
     * webscoket建立好链接之后的处理函数--连接建立后的准备工作
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        //将当前的连接的用户会话放入MAP,key是用户编号
        Users loginUser = (Users) webSocketSession.getAttributes().get("loginUser");
        USER_SOCKETSESSION_MAP.put(String.valueOf(loginUser.getId()), webSocketSession);

        //群发消息告知大家
        Messages msg = new Messages();
        msg.setText("风骚的【"+loginUser.getNickname()+"】踩着轻盈的步伐来啦。。。大家欢迎！");
//        msg.setDate(new Date());

        //获取所有在线的WebSocketSession对象集合
        Set<Map.Entry<String, WebSocketSession>> entrySet = USER_SOCKETSESSION_MAP.entrySet();
        //将最新的所有的在线人列表放入消息对象的list集合中，用于页面显示
//        for (Map.Entry<String, WebSocketSession> entry : entrySet) {
//            msg.getUserList().add((User)entry.getValue().getAttributes().get("loginUser"));
//        }

        Gson gson = new Gson();

        //将消息转换为json
        TextMessage message = new TextMessage( gson.toJson(msg));
        //群发消息
        sendMessageToAll(message);



    }

    @Override
    /**
     * 客户端发送服务器的消息时的处理函数，在这里收到消息之后可以分发消息
     */
    //处理消息：当一个新的WebSocket到达的时候，会被调用（在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理）
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> message) throws Exception {
        //如果消息没有任何内容，则直接返回
        if(message.getPayloadLength()==0) return;
        //反序列化服务端收到的json消息
        JSONObject jsonObject = new JSONObject(message.getPayload().toString());
        Messages msg = new Messages();
//        set datetime = now
        Timestamp time = new Timestamp(new java.util.Date().getTime());
        msg.setSendTime(time);
        msg.setText(jsonObject.getString("text"));
        msg.setSenderId(jsonObject.getInt("sender_id"));
        msg.setSenderName(jsonObject.getString("sender_name"));
        msg.setReceiverId(jsonObject.getInt("receiver_id"));
        msg.setReceiverName(jsonObject.getString("receiver_name"));
        //处理html的字符，转义：
        String text = msg.getText();
        //转换为HTML转义字符表示
        String htmlEscapeText = HtmlUtils.htmlEscape(text);
        msg.setText(htmlEscapeText);

        this.messagesServiceI.saveMessage(msg);

        System.out.println("訊息:"+ message.getPayload().toString());
        Gson gson = new Gson();
        CharSequence test = gson.toJson(msg);
        sendMessageToUser(String.valueOf(msg.getReceiverId()), new TextMessage(test));

    }

    @Override
    /**
     * 消息传输过程中出现的异常处理函数
     * 处理传输错误：处理由底层WebSocket消息传输过程中发生的异常
     */
    public void handleTransportError(WebSocketSession webSocketSession, Throwable exception) throws Exception {
        // 记录日志，准备关闭连接
        System.out.println("Websocket斷線:" + webSocketSession.getId() + "已經斷線");
        //一旦发生异常，强制用户下线，关闭session
        if (webSocketSession.isOpen()) {
            webSocketSession.close();
        }

        //群发消息告知大家
        Messages msg = new Messages();
//        Timestamp time = new Timestamp(new java.util.Date().getTime());
//        msg.setSendTime(new Date());

        //获取异常的用户的会话中的用户编号
        Users loginUser=(Users) webSocketSession.getAttributes().get("loginUser");
        //获取所有的用户的会话
        Set<Map.Entry<String, WebSocketSession>> entrySet = USER_SOCKETSESSION_MAP.entrySet();
        //并查找出在线用户的WebSocketSession（会话），将其移除（不再对其发消息了。。）
        for (Map.Entry<String, WebSocketSession> entry : entrySet) {
            if(entry.getKey().equals(String.valueOf(loginUser.getId()))){
//                msg.setText("万众瞩目的【"+loginUser.getNickname()+"】已经退出。。。！");
                //清除在线会话
                USER_SOCKETSESSION_MAP.remove(entry.getKey());
                //记录日志：
                System.out.println("Socket移除:用戶ID" + entry.getKey());
                break;
            }
        }

//        //并查找出在线用户的WebSocketSession（会话），将其移除（不再对其发消息了。。）
//        for (Map.Entry<String, WebSocketSession> entry : entrySet) {
//            msg.getUserList().add((User)entry.getValue().getAttributes().get("loginUser"));
//        }
//
//        Gson gson = new Gson();
//        TextMessage message = new TextMessage(gson.toJson(msg));
//        sendMessageToAll(message);

    }

    @Override
    /**
     * websocket链接关闭的回调
     * 连接关闭后：一般是回收资源等
     */
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        // 记录日志，准备关闭连接
        System.out.println("Websocket正常斷開:" + webSocketSession.getId() + "已經關閉");

        //群发消息告知大家
//        Messages msg = new Messages();
//        Timestamp time = new Timestamp(new java.util.Date().getTime());
//        msg.setSendTime(time);

        //获取异常的用户的会话中的用户编号
        Users loginUser= (Users) webSocketSession.getAttributes().get("loginUser");
        Set<Map.Entry<String, WebSocketSession>> entrySet = USER_SOCKETSESSION_MAP.entrySet();
        //并查找出在线用户的WebSocketSession（会话），将其移除（不再对其发消息了。。）
        for (Map.Entry<String, WebSocketSession> entry : entrySet) {
            if(entry.getKey().equals(String.valueOf(loginUser.getId()))){
                //群发消息告知大家
//                msg.setText("万众瞩目的【"+loginUser.getNickname()+"】已经有事先走了，大家继续聊...");
                //清除在线会话
                USER_SOCKETSESSION_MAP.remove(entry.getKey());
                //记录日志：
                System.out.println("Socket移除:用户ID" + entry.getKey());
                break;
            }
        }

//        //并查找出在线用户的WebSocketSession（会话），将其移除（不再对其发消息了。。）
//        for (Map.Entry<String, WebSocketSession> entry : entrySet) {
//            msg.getUserList().add((User)entry.getValue().getAttributes().get("loginUser"));
//        }
//
//        Gson gson  = new Gson();
//        TextMessage message = new TextMessage(gson.toJson(msg));
//        sendMessageToAll(message);
    }

    @Override
    /**
     * 是否支持处理拆分消息，返回true返回拆分消息
     */
    //是否支持部分消息：如果设置为true，那么一个大的或未知尺寸的消息将会被分割，并会收到多次消息（会通过多次调用方法handleMessage(WebSocketSession, WebSocketMessage). ）
    //如果分为多条消息，那么可以通过一个api：org.springframework.web.socket.WebSocketMessage.isLast() 是否是某条消息的最后一部分。
    //默认一般为false，消息不分割
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     *
     * 说明：给某个人发信息
     * @param id
     * @param message
     */
    private void sendMessageToUser(String id, TextMessage message) throws IOException {
        //获取到要接收消息的用户的session
        WebSocketSession webSocketSession = USER_SOCKETSESSION_MAP.get(id);

        if (webSocketSession != null && webSocketSession.isOpen()) {
            //发送消息
            System.out.print("發送消息～\n");
            webSocketSession.sendMessage(message);
        }
    }

    /**
     *
     * 说明：群发信息：给所有在线用户发送消息
     */
    private void sendMessageToAll(final TextMessage message){
        //对用户发送的消息内容进行转义

        //获取到所有在线用户的SocketSession对象
        Set<Map.Entry<String, WebSocketSession>> entrySet = USER_SOCKETSESSION_MAP.entrySet();
        for (Map.Entry<String, WebSocketSession> entry : entrySet) {
            //某用户的WebSocketSession
            final WebSocketSession webSocketSession = entry.getValue();
            //判断连接是否仍然打开的
            if(webSocketSession.isOpen()){
                //开启多线程发送消息（效率高）
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            if (webSocketSession.isOpen()) {
                                webSocketSession.sendMessage(message);
                            }
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }).start();

            }
        }
    }

}
