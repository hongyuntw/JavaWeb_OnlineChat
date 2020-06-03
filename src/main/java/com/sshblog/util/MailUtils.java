package com.sshblog.util;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtils implements Runnable {
    private String email;// 收件人郵箱
    private String code;// 啟用碼
//    default is send mail account
    private Boolean mailType;
    public MailUtils(String email, String code,Boolean type) {
        this.email = email;
        this.code = code;
        this.mailType = type;
    }
    public void run() {
// 1.建立連線物件javax.mail.Session
// 2.建立郵件物件 javax.mail.Message
// 3.傳送一封啟用郵件
        String from = "harrychiang0@gmail.com";// 發件人電子郵箱
        String host = "smtp.gmail.com";
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);// 設定郵件伺服器
        properties.put("mail.smtp.auth", "true");// 開啟認證
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.port","587");

        try {

// 1.獲取預設session物件
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("harrychiang0@gmail.com", "rhahlrimaffucgjy"); // 發件人郵箱賬號、授權碼
                }
            });
// 2.建立郵件物件
            Message message = new MimeMessage(session);
// 2.1設定發件人
            message.setFrom(new InternetAddress(from));
// 2.2設定接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
// 2.3設定郵件主題
            String content="";
            if(mailType){
                message.setSubject("帳號啟用");
                content = "<html><head></head><body><h1>這是一封啟用郵件,啟用請點選以下連結</h1><h3>" +
                        "<a href='http://localhost:7777/active?code="+code+"'>啟用帳戶"+
                        "</a></h3></body></html>";
            }
            else{
                message.setSubject("重設密碼");
                content = "<html><head></head><body><h1>如果想重設密碼,請點選以下連結</h1><h3>" +
                        "<a href='http://localhost:7777/resetPasswordProcess?code="+code+"'>重設密碼"+
                        "</a></h3></body></html>";
            }
// 2.4設定郵件內容

            message.setContent(content, "text/html;charset=UTF-8");
// 3.傳送郵件
            Transport.send(message);
            System.out.println("郵件成功傳送!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}