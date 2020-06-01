<%@ page import="com.sshblog.entity.Messages" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.sshblog.entity.Users" %><%--
  Created by IntelliJ IDEA.
  User: chianghongyun
  Date: 2020/5/1
  Time: 10:39 下午
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../include.jsp" %>

<%
    String path = request.getContextPath();
    // 获得项目完全路径（假设你的项目叫MyApp，那么获得到的地址就是http://localhost:8080/MyApp/）:
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    String receiver_id = request.getParameter("rID");
    String receiver_name = "";
    Users currentReceiveUser = new Users();
    if (receiver_id != null || receiver_id != "") {
        for (Users user : (List<Users>) request.getAttribute("allUsers")) {
            if (String.valueOf(user.getId()).equals(receiver_id)) {
                currentReceiveUser = user;
                receiver_name = user.getNickname();
            }
        }
    }
%>

<html>
<head>
    <title>聊天首頁</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src='//production-assets.codepen.io/assets/editor/live/console_runner-079c09a0e3b9ff743e39ee2d5637b9216b3545af0de366d4b9aad9dc87e26bfd.js'></script>
    <script src='//production-assets.codepen.io/assets/editor/live/events_runner-73716630c22bbc8cff4bd0f07b135f00a0bdc5d14629260c3ec49e5606f98fdd.js'></script>
    <script src='//production-assets.codepen.io/assets/editor/live/css_live_reload_init-2c0dc5167d60a5af3ee189d570b1835129687ea2a61bee3513dee3a50c115a77.js'></script>
    <meta charset='UTF-8'>
    <meta name="robots" content="noindex">
    <link rel="shortcut icon" type="image/x-icon"
          href="//production-assets.codepen.io/assets/favicon/favicon-8ea04875e70c4b0bb41da869e81236e54394d63638a1ef12fa558a4a835f1164.ico"/>
    <link rel="mask-icon" type=""
          href="//production-assets.codepen.io/assets/favicon/logo-pin-f2d2b6d2c61838f7e76325261b7195c27224080bc099486ddd6dccb469b8e8e6.svg"
          color="#111"/>
    <link rel="canonical" href="https://codepen.io/emilcarlsson/pen/ZOQZaV?limit=all&page=74&q=contact+"/>
    <link href='https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,600,700,300' rel='stylesheet'
          type='text/css'>

    <script src="https://use.typekit.net/hoy3lrg.js"></script>
    <script>try {
        Typekit.load({async: true});
    } catch (e) {
    }</script>
    <link rel='stylesheet prefetch' href='https://cdnjs.cloudflare.com/ajax/libs/meyer-reset/2.0/reset.min.css'>
    <link rel='stylesheet prefetch'
          href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.2/css/font-awesome.min.css'>
    <style class="cp-pen-styles">body {
        display: flex;
        align-items: center;
        justify-content: center;
        min-height: 100vh;
        background: #27ae60;
        font-family: "proxima-nova", "Source Sans Pro", sans-serif;
        font-size: 1em;
        letter-spacing: 0.1px;
        color: #32465a;
        text-rendering: optimizeLegibility;
        text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.004);
        -webkit-font-smoothing: antialiased;
    }

    #frame {
        width: 95%;
        min-width: 360px;
        max-width: 1000px;
        height: 92vh;
        min-height: 300px;
        max-height: 720px;
        background: #E6EAEA;
    }

    @media screen and (max-width: 360px) {
        #frame {
            width: 100%;
            height: 100vh;
        }
    }

    #frame #sidepanel {
        float: left;
        min-width: 280px;
        max-width: 340px;
        width: 40%;
        height: 100%;
        background: #2c3e50;
        color: #f5f5f5;
        overflow: hidden;
        position: relative;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel {
            width: 58px;
            min-width: 58px;
        }
    }

    #frame #sidepanel #profile {
        width: 80%;
        margin: 25px auto;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile {
            width: 100%;
            margin: 0 auto;
            padding: 5px 0 0 0;
            background: #32465a;
        }
    }

    #frame #sidepanel #profile.expanded .wrap {
        height: 210px;
        line-height: initial;
    }

    #frame #sidepanel #profile.expanded .wrap p {
        margin-top: 20px;
    }

    #frame #sidepanel #profile.expanded .wrap i.expand-button {
        -moz-transform: scaleY(-1);
        -o-transform: scaleY(-1);
        -webkit-transform: scaleY(-1);
        transform: scaleY(-1);
        filter: FlipH;
        -ms-filter: "FlipH";
    }

    #frame #sidepanel #profile .wrap {
        height: 60px;
        line-height: 60px;
        overflow: hidden;
        -moz-transition: 0.3s height ease;
        -o-transition: 0.3s height ease;
        -webkit-transition: 0.3s height ease;
        transition: 0.3s height ease;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile .wrap {
            height: 55px;
        }
    }

    #frame #sidepanel #profile .wrap img {
        width: 50px;
        border-radius: 50%;
        padding: 3px;
        border: 2px solid #e74c3c;
        height: auto;
        float: left;
        cursor: pointer;
        -moz-transition: 0.3s border ease;
        -o-transition: 0.3s border ease;
        -webkit-transition: 0.3s border ease;
        transition: 0.3s border ease;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile .wrap img {
            width: 40px;
            margin-left: 4px;
        }
    }

    #frame #sidepanel #profile .wrap img.online {
        border: 2px solid #2ecc71;
    }

    #frame #sidepanel #profile .wrap img.away {
        border: 2px solid #f1c40f;
    }

    #frame #sidepanel #profile .wrap img.busy {
        border: 2px solid #e74c3c;
    }

    #frame #sidepanel #profile .wrap img.offline {
        border: 2px solid #95a5a6;
    }

    #frame #sidepanel #profile .wrap p {
        float: left;
        margin-left: 15px;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile .wrap p {
            display: none;
        }
    }

    #frame #sidepanel #profile .wrap i.expand-button {
        float: right;
        margin-top: 23px;
        font-size: 0.8em;
        cursor: pointer;
        color: #435f7a;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile .wrap i.expand-button {
            display: none;
        }
    }

    #frame #sidepanel #profile .wrap #status-options {
        position: absolute;
        opacity: 0;
        visibility: hidden;
        width: 150px;
        margin: 70px 0 0 0;
        border-radius: 6px;
        z-index: 99;
        line-height: initial;
        background: #435f7a;
        -moz-transition: 0.3s all ease;
        -o-transition: 0.3s all ease;
        -webkit-transition: 0.3s all ease;
        transition: 0.3s all ease;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile .wrap #status-options {
            width: 58px;
            margin-top: 57px;
        }
    }

    #frame #sidepanel #profile .wrap #status-options.active {
        opacity: 1;
        visibility: visible;
        margin: 75px 0 0 0;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile .wrap #status-options.active {
            margin-top: 62px;
        }
    }

    #frame #sidepanel #profile .wrap #status-options:before {
        content: '';
        position: absolute;
        width: 0;
        height: 0;
        border-left: 6px solid transparent;
        border-right: 6px solid transparent;
        border-bottom: 8px solid #435f7a;
        margin: -8px 0 0 24px;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile .wrap #status-options:before {
            margin-left: 23px;
        }
    }

    #frame #sidepanel #profile .wrap #status-options ul {
        overflow: hidden;
        border-radius: 6px;
    }

    #frame #sidepanel #profile .wrap #status-options ul li {
        padding: 15px 0 30px 18px;
        display: block;
        cursor: pointer;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile .wrap #status-options ul li {
            padding: 15px 0 35px 22px;
        }
    }

    #frame #sidepanel #profile .wrap #status-options ul li:hover {
        background: #496886;
    }

    #frame #sidepanel #profile .wrap #status-options ul li span.status-circle {
        position: absolute;
        width: 10px;
        height: 10px;
        border-radius: 50%;
        margin: 5px 0 0 0;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile .wrap #status-options ul li span.status-circle {
            width: 14px;
            height: 14px;
        }
    }

    #frame #sidepanel #profile .wrap #status-options ul li span.status-circle:before {
        content: '';
        position: absolute;
        width: 14px;
        height: 14px;
        margin: -3px 0 0 -3px;
        background: transparent;
        border-radius: 50%;
        z-index: 0;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile .wrap #status-options ul li span.status-circle:before {
            height: 18px;
            width: 18px;
        }
    }

    #frame #sidepanel #profile .wrap #status-options ul li p {
        padding-left: 12px;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #profile .wrap #status-options ul li p {
            display: none;
        }
    }

    #frame #sidepanel #profile .wrap #status-options ul li#status-online span.status-circle {
        background: #2ecc71;
    }

    #frame #sidepanel #profile .wrap #status-options ul li#status-online.active span.status-circle:before {
        border: 1px solid #2ecc71;
    }

    #frame #sidepanel #profile .wrap #status-options ul li#status-away span.status-circle {
        background: #f1c40f;
    }

    #frame #sidepanel #profile .wrap #status-options ul li#status-away.active span.status-circle:before {
        border: 1px solid #f1c40f;
    }

    #frame #sidepanel #profile .wrap #status-options ul li#status-busy span.status-circle {
        background: #e74c3c;
    }

    #frame #sidepanel #profile .wrap #status-options ul li#status-busy.active span.status-circle:before {
        border: 1px solid #e74c3c;
    }

    #frame #sidepanel #profile .wrap #status-options ul li#status-offline span.status-circle {
        background: #95a5a6;
    }

    #frame #sidepanel #profile .wrap #status-options ul li#status-offline.active span.status-circle:before {
        border: 1px solid #95a5a6;
    }

    #frame #sidepanel #profile .wrap #expanded {
        padding: 100px 0 0 0;
        display: block;
        line-height: initial !important;
    }

    #frame #sidepanel #profile .wrap #expanded label {
        float: left;
        clear: both;
        margin: 0 8px 5px 0;
        padding: 5px 0;
    }

    #frame #sidepanel #profile .wrap #expanded input {
        border: none;
        margin-bottom: 6px;
        background: #32465a;
        border-radius: 3px;
        color: #f5f5f5;
        padding: 7px;
        width: calc(100% - 43px);
    }

    #frame #sidepanel #profile .wrap #expanded input:focus {
        outline: none;
        background: #435f7a;
    }

    #frame #sidepanel #search {
        border-top: 1px solid #32465a;
        border-bottom: 1px solid #32465a;
        font-weight: 300;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #search {
            display: none;
        }
    }

    #frame #sidepanel #search label {
        position: absolute;
        margin: 10px 0 0 20px;
    }

    #frame #sidepanel #search input {
        font-family: "proxima-nova", "Source Sans Pro", sans-serif;
        padding: 10px 0 10px 46px;
        width: calc(100% - 25px);
        border: none;
        background: #32465a;
        color: #f5f5f5;
    }

    #frame #sidepanel #search input:focus {
        outline: none;
        background: #435f7a;
    }

    #frame #sidepanel #search input::-webkit-input-placeholder {
        color: #f5f5f5;
    }

    #frame #sidepanel #search input::-moz-placeholder {
        color: #f5f5f5;
    }

    #frame #sidepanel #search input:-ms-input-placeholder {
        color: #f5f5f5;
    }

    #frame #sidepanel #search input:-moz-placeholder {
        color: #f5f5f5;
    }

    #frame #sidepanel #contacts {
        height: calc(100% - 177px);
        overflow-y: scroll;
        overflow-x: hidden;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #contacts {
            height: calc(100% - 149px);
            overflow-y: scroll;
            overflow-x: hidden;
        }

        #frame #sidepanel #contacts::-webkit-scrollbar {
            display: none;
        }
    }

    #frame #sidepanel #contacts.expanded {
        height: calc(100% - 334px);
    }

    #frame #sidepanel #contacts::-webkit-scrollbar {
        width: 8px;
        background: #2c3e50;
    }

    #frame #sidepanel #contacts::-webkit-scrollbar-thumb {
        background-color: #243140;
    }

    #frame #sidepanel #contacts ul li.contact {
        position: relative;
        padding: 10px 0 15px 0;
        font-size: 0.9em;
        cursor: pointer;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #contacts ul li.contact {
            padding: 6px 0 46px 8px;
        }
    }

    #frame #sidepanel #contacts ul li.contact:hover {
        background: #32465a;
    }

    #frame #sidepanel #contacts ul li.contact.active {
        background: #32465a;
        border-right: 5px solid #435f7a;
    }

    #frame #sidepanel #contacts ul li.contact.active span.contact-status {
        border: 2px solid #32465a !important;
    }

    #frame #sidepanel #contacts ul li.contact .wrap {
        width: 88%;
        margin: 0 auto;
        position: relative;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #contacts ul li.contact .wrap {
            width: 100%;
        }
    }

    #frame #sidepanel #contacts ul li.contact .wrap span {
        position: absolute;
        left: 0;
        margin: -2px 0 0 -2px;
        width: 10px;
        height: 10px;
        border-radius: 50%;
        border: 2px solid #2c3e50;
        background: #95a5a6;
    }

    #frame #sidepanel #contacts ul li.contact .wrap span.online {
        background: #2ecc71;
    }

    #frame #sidepanel #contacts ul li.contact .wrap span.away {
        background: #f1c40f;
    }

    #frame #sidepanel #contacts ul li.contact .wrap span.busy {
        background: #e74c3c;
    }

    #frame #sidepanel #contacts ul li.contact .wrap img {
        width: 40px;
        border-radius: 50%;
        float: left;
        margin-right: 10px;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #contacts ul li.contact .wrap img {
            margin-right: 0px;
        }
    }

    #frame #sidepanel #contacts ul li.contact .wrap .meta {
        padding: 5px 0 0 0;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #contacts ul li.contact .wrap .meta {
            display: none;
        }
    }

    #frame #sidepanel #contacts ul li.contact .wrap .meta .name {
        font-weight: 600;
    }

    #frame #sidepanel #contacts ul li.contact .wrap .meta .preview {
        margin: 5px 0 0 0;
        padding: 0 0 1px;
        font-weight: 400;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        -moz-transition: 1s all ease;
        -o-transition: 1s all ease;
        -webkit-transition: 1s all ease;
        transition: 1s all ease;
    }

    #frame #sidepanel #contacts ul li.contact .wrap .meta .preview span {
        position: initial;
        border-radius: initial;
        background: none;
        border: none;
        padding: 0 2px 0 0;
        margin: 0 0 0 1px;
        opacity: .5;
    }

    #frame #sidepanel #bottom-bar {
        position: absolute;
        width: 100%;
        bottom: 0;
    }

    #frame #sidepanel #bottom-bar button {
        float: left;
        border: none;
        width: 100%;
        padding: 10px 0;
        background: #32465a;
        color: #f5f5f5;
        cursor: pointer;
        font-size: 0.85em;
        font-family: "proxima-nova", "Source Sans Pro", sans-serif;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #bottom-bar button {
            float: none;
            width: 100%;
            padding: 15px 0;
        }
    }

    #frame #sidepanel #bottom-bar button:focus {
        outline: none;
    }

    #frame #sidepanel #bottom-bar button:nth-child(1) {
        border-right: 1px solid #2c3e50;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #bottom-bar button:nth-child(1) {
            border-right: none;
            border-bottom: 1px solid #2c3e50;
        }
    }

    #frame #sidepanel #bottom-bar button:hover {
        background: #435f7a;
    }

    #frame #sidepanel #bottom-bar button i {
        margin-right: 3px;
        font-size: 1em;
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #bottom-bar button i {
            font-size: 1.3em;
        }
    }

    @media screen and (max-width: 735px) {
        #frame #sidepanel #bottom-bar button span {
            display: none;
        }
    }

    #frame .content {
        float: right;
        width: 60%;
        height: 100%;
        overflow: hidden;
        position: relative;
    }

    @media screen and (max-width: 735px) {
        #frame .content {
            width: calc(100% - 58px);
            min-width: 300px !important;
        }
    }

    @media screen and (min-width: 900px) {
        #frame .content {
            width: calc(100% - 340px);
        }
    }

    #frame .content .contact-profile {
        width: 100%;
        height: 60px;
        line-height: 60px;
        background: #f5f5f5;
    }

    #frame .content .contact-profile img {
        width: 40px;
        border-radius: 50%;
        float: left;
        margin: 9px 12px 0 9px;
    }

    #frame .content .contact-profile p {
        float: left;
    }

    #frame .content .contact-profile .social-media {
        float: right;
    }

    #frame .content .contact-profile .social-media i {
        margin-left: 14px;
        cursor: pointer;
    }

    #frame .content .contact-profile .social-media i:nth-last-child(1) {
        margin-right: 20px;
    }

    #frame .content .contact-profile .social-media i:hover {
        color: #435f7a;
    }

    #frame .content .messages {
        height: auto;
        min-height: calc(100% - 93px);
        max-height: calc(100% - 93px);
        overflow-y: scroll;
        overflow-x: hidden;
    }

    @media screen and (max-width: 735px) {
        #frame .content .messages {
            max-height: calc(100% - 105px);
        }
    }

    #frame .content .messages::-webkit-scrollbar {
        width: 8px;
        background: transparent;
    }

    #frame .content .messages::-webkit-scrollbar-thumb {
        background-color: rgba(0, 0, 0, 0.3);
    }

    #frame .content .messages ul li {
        display: inline-block;
        clear: both;
        float: left;
        margin: 15px 15px 5px 15px;
        width: calc(100% - 25px);
        font-size: 0.9em;
    }

    #frame .content .messages ul li:nth-last-child(1) {
        margin-bottom: 20px;
    }

    #frame .content .messages ul li.replies img {
        margin: 6px 8px 0 0;
    }

    #frame .content .messages ul li.replies p {
        background: #435f7a;
        color: #f5f5f5;
        word-break: break-all;
    }

    #frame .content .messages ul li.sent img {
        float: right;
        margin: 6px 0 0 8px;
    }

    #frame .content .messages ul li.sent p {
        background: #f5f5f5;
        float: right;
        word-break: break-all;

    }

    #frame .content .messages ul li img {
        width: 22px;
        border-radius: 50%;
        float: left;
    }

    #frame .content .messages ul li p {
        display: inline-block;
        padding: 10px 15px;
        border-radius: 20px;
        max-width: 205px;
        line-height: 130%;
        word-break: break-all;

    }

    @media screen and (min-width: 735px) {
        #frame .content .messages ul li p {
            max-width: 300px;
        }
    }

    #frame .content .message-input {
        position: absolute;
        bottom: 0;
        width: 100%;
        z-index: 99;
        word-break: break-all;

    }

    #frame .content .message-input .wrap {
        position: relative;
    }

    #frame .content .message-input .wrap input {
        font-family: "proxima-nova", "Source Sans Pro", sans-serif;
        float: left;
        border: none;
        width: calc(100% - 50px);
        padding: 11px 32px 10px 8px;
        font-size: 0.8em;
        color: #32465a;
        word-break: break-all;

    }

    @media screen and (max-width: 735px) {
        #frame .content .message-input .wrap input {
            padding: 15px 32px 16px 8px;
        }
    }

    #frame .content .message-input .wrap input:focus {
        outline: none;
    }

    #frame .content .message-input .wrap .attachment {
        position: absolute;
        right: 60px;
        z-index: 4;
        margin-top: 10px;
        font-size: 1.1em;
        color: #435f7a;
        opacity: .5;
        cursor: pointer;
    }

    @media screen and (max-width: 735px) {
        #frame .content .message-input .wrap .attachment {
            margin-top: 17px;
            right: 65px;
        }
    }

    #frame .content .message-input .wrap .attachment:hover {
        opacity: 1;
    }

    #frame .content .message-input .wrap button {
        float: right;
        border: none;
        width: 50px;
        padding: 12px 0;
        cursor: pointer;
        background: #32465a;
        color: #f5f5f5;
    }

    @media screen and (max-width: 735px) {
        #frame .content .message-input .wrap button {
            padding: 16px 0;
        }
    }

    #frame .content .message-input .wrap button:hover {
        background: #435f7a;
    }

    #frame .content .message-input .wrap button:focus {
        outline: none;
    }
    </style>
</head>
<body>
<div id="frame">
    <div id="sidepanel">
        <div id="profile">
            <div class="wrap">
                <img id="profile-img" src="http://emilcarlsson.se/assets/mikeross.png" class="online" alt=""/>
                <p>${sessionScope.user.nickname}</p>
                <%--                <i class="fa fa-chevron-down expand-button" aria-hidden="true"></i>--%>
                <div id="status-options">
                    <ul>
                        <li id="status-online" class="active"><span class="status-circle"></span>
                            <p>Online</p></li>
                        <li id="status-away"><span class="status-circle"></span>
                            <p>Away</p></li>
                        <li id="status-busy"><span class="status-circle"></span>
                            <p>Busy</p></li>
                        <li id="status-offline"><span class="status-circle"></span>
                            <p>Offline</p></li>
                    </ul>
                </div>
                <%--                <div id="expanded">--%>
                <%--                    <label for="twitter"><i class="fa fa-facebook fa-fw" aria-hidden="true"></i></label>--%>
                <%--                    <input name="twitter" type="text" value="mikeross"/>--%>
                <%--                    <label for="twitter"><i class="fa fa-twitter fa-fw" aria-hidden="true"></i></label>--%>
                <%--                    <input name="twitter" type="text" value="ross81"/>--%>
                <%--                    <label for="twitter"><i class="fa fa-instagram fa-fw" aria-hidden="true"></i></label>--%>
                <%--                    <input name="twitter" type="text" value="mike.ross"/>--%>
                <%--                </div>--%>
            </div>
        </div>
        <%--        <div id="search">--%>
        <%--            <label for=""><i class="fa fa-search" aria-hidden="true"></i></label>--%>
        <%--            <input type="text" placeholder="Search contacts..."/>--%>
        <%--        </div>--%>
        <div id="contacts">
            <ul>
                <%
                    Map<String, List<Messages>> allMessages = (Map<String, List<Messages>>) request.getAttribute("allMessages");
                    Users login_user = (Users) session.getAttribute("user");
                    String login_user_id = String.valueOf(login_user.getId());
                %>
                <%--                <c:forEach items="${allUsers}" var="other_user">--%>
                <% for (Users other_user : (List<Users>) request.getAttribute("allUsers")) {
                    String other_user_id = String.valueOf(other_user.getId());
                    if (!other_user_id.equals(login_user_id)) {
                %>
                <%--                    <c:if test="${not other_user.email.equals(sessionScope.user.email)}">--%>
                <li class="contact" onclick="left_list_onclick(this)" id="left_list<%=other_user_id%>">
                    <div class="wrap">
                        <span class="contact-status online"></span>
                        <img src="http://emilcarlsson.se/assets/louislitt.png" alt=""/>
                        <div class="meta">
                            <%
                                List<Messages> msg_list = allMessages.get(other_user_id);
                                String last_msg = "";
                                if (!msg_list.isEmpty()) {
                                    last_msg = msg_list.get(msg_list.size() - 1).getText();
                                }
                            %>
                            <p class="name"><%=other_user.getNickname()%>
                            </p>
                            <p class="preview"><%=last_msg%>
                            </p>
                        </div>
                    </div>
                </li>
                <%--                    </c:if>--%>
                <% }
                } %>
                <%--                </c:forEach>--%>
                <%--                <li class="contact active">--%>
                <%--                    <div class="wrap">--%>
                <%--                        <span class="contact-status busy"></span>--%>
                <%--                        <img src="http://emilcarlsson.se/assets/harveyspecter.png" alt=""/>--%>
                <%--                        <div class="meta">--%>
                <%--                            <p class="name">Harvey Specter</p>--%>
                <%--                            <p class="preview">Wrong. You take the gun, or you pull out a bigger one. Or, you call their--%>
                <%--                                bluff. Or, you do any one of a hundred and forty six other things.</p>--%>
                <%--                        </div>--%>
                <%--                    </div>--%>
                <%--                </li>--%>

            </ul>
        </div>
        <div id="bottom-bar">
            <%--            <button id="addcontact"><i class="fa fa-user-plus fa-fw" aria-hidden="true"></i> <span>Add contact</span>--%>
            <%--            </button>--%>
            <form method="get" action="<c:url value="/logout" />">
                <button id="settings" type="submit"><i class="fa fa-cog fa-fw" aria-hidden="true"></i>
                    <span>Logout</span></button>
            </form>
        </div>
    </div>
    <div class="content">
        <div class="contact-profile">
            <img src="http://emilcarlsson.se/assets/harveyspecter.png" alt=""/>
            <p><%=receiver_name%></p>
        </div>
        <div id="messages_div" class="messages">
            <ul>
                <%
                    if (receiver_id != null && receiver_id != "") {
                        List<Messages> chat_msgs = allMessages.get(receiver_id);
                        if (!chat_msgs.isEmpty()) {
                            for (Messages msg : chat_msgs) {
                                String class_name;
                                String text = msg.getText();
                                if (msg.getSenderId() == login_user.getId()) {
                                    class_name = "sent";
                                } else {
                                    class_name = "replies";
                                }%>
                <li class="<%=class_name%>">
                    <img src="http://emilcarlsson.se/assets/mikeross.png" alt=""/>
                    <p><%=text%></p>
                </li>
                <%
                            }
                        }
                    }

                %>
<%--                <li class="sent">--%>
<%--                    <img src="http://emilcarlsson.se/assets/mikeross.png" alt=""/>--%>
<%--                    <p>How the hell am I supposed to get a jury to believe you when I am not even sure that I do?!</p>--%>
<%--                </li>--%>
            </ul>
        </div>
        <%--        here to input message--%>
        <div class="message-input">
            <div class="wrap">
                <input id="msg" type="text" placeholder="Write your message..."/>
                <button onclick="sendBtn_onclick()" id="sendBtn" class="submit"><i class="fa fa-paper-plane"></i>
                </button>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    var path = 'localhost:8080/';
    var uid = '${sessionScope.user.id}';
    //发送人编号
    <%--var sender_id='${sessionScope.user.id}';--%>
    <%--var sender_name='${sessionScope.user.nickname}';--%>
    var current_user_id = '${sessionScope.user.id}';
    var current_user_name = '${sessionScope.user.nickname}';
    var current_receiver_id = '<%= currentReceiveUser.getId()%>';
    var current_receiver_name = '<%= currentReceiveUser.getNickname()%>';
    //接收人编号

    // var to = "2";
    // 创建一个Socket实例
    //参数为URL，ws表示WebSocket协议。onopen、onclose和onmessage方法把事件连接到Socket实例上。每个方法都提供了一个事件，以表示Socket的状态。
    var websocket;
    //不同浏览器的WebSocket对象类型不同
    //alert("ws://" + path + "/ws?uid="+uid);
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://" + path + "ws");
        console.log("=============WebSocket");
        //火狐
    } else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://" + path + "ws");
        console.log("=============MozWebSocket");
    } else {
        websocket = new SockJS("http://" + path + "ws/sockjs");
        console.log("=============SockJS");
    }

    console.log("ws://" + path + "ws");

    //打开Socket,
    websocket.onopen = function (event) {
        console.log("WebSocket:已連接 ");
    }

    // 监听消息
    //onmessage事件提供了一个data属性，它可以包含消息的Body部分。消息的Body部分必须是一个字符串，可以进行序列化/反序列化操作，以便传递更多的数据。
    websocket.onmessage = function (event) {
        // console.log('Client received a message',event);
        //var data=JSON.parse(event.data);
        var data = $.parseJSON(event.data);
        console.log("WebSocket:收到一條訊息", data);

        //2种推送的消息
        //1.用户聊天信息：发送消息触发
        //2.系统消息：登录和退出触发

        // //判断是否是欢迎消息（没用户编号的就是欢迎消息）
        if (data.senderId == 0 || data.senderId == null || data.senderId == "" || data.receiverId == 0 || data.receiverId == null || data.receiverId == "") {
            //===系统消息
            // $("#contentUl").append("<li><b>"+data.date+"</b><em>系统消息：</em><span>"+data.text+"</span></li>");
            // //刷新在线用户列表
            // $("#chatOnline").html("在线用户("+data.userList.length+")人");
            // $("#chatUserList").empty();
            // $(data.userList).each(function(){
            //     $("#chatUserList").append("<li>"+this.nickname+"</li>");
            // });
            return

        }

        console.log(data.text);
        //收到別人傳過來的訊息

        if (data.receiverId == current_user_id) {
            // var html_msg = '<li class="replies"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p>' + data.text + '</p></li>';
            // console.log(html_msg);
            $('<li class="replies"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p>' + data.text + '</p></li>').appendTo($('.messages ul'));
            //更改左側對話欄
            $('.contact.active .preview').html('<span> data.senderName: </span>' + data.text);
            // $(".messages").animate({scrollTop: $(document).height()}, "fast");
            scrollToBottom();
            // $("#contentUl").append("<li><span  style='display:block; float:right;'><em>"+data.sender_name+"</em><span>"+data.text+"</span><b>"+data.send_time+"</b></span></li><br/>");
            // scrollToBottom();
        }
    };

    // 监听WebSocket的关闭
    websocket.onclose = function (event) {
        $("#contentUl").append("<li><b>" + new Date().Format("yyyy-MM-dd hh:mm:ss") + "</b><em>系统消息：</em><span>连接已断开！</span></li>");
        scrollToBottom();
        console.log("WebSocket:已關閉：Client notified socket has closed", event);
    };

    //监听异常
    websocket.onerror = function (event) {
        $("#contentUl").append("<li><b>" + new Date().Format("yyyy-MM-dd hh:mm:ss") + "</b><em>系统消息：</em><span>连接异常，建议重新登录</span></li>");
        scrollToBottom();
        console.log("WebSocket:發生錯誤 ", event);
    };

    //onload初始化
    $(function () {
        //发送消息
        // $("#sendBtn").on("click", function () {
        //     sendMsg();
        // });
        //给退出聊天绑定事件
        $("#exitBtn").on("click", function () {
            closeWebsocket();
            location.href = "${pageContext.request.contextPath}/index.jsp";
        });

        //给输入框绑定事件
        $("#msg").on("keydown", function (event) {
            keySend(event);
        });
        //初始化时如果有消息，则滚动条到最下面：
        // scrollToBottom();
    });

    function sendBtn_onclick() {
        console.log(123);
        sendMsg();
        scrollToBottom();
    }

    //使用ctrl+回车快捷键发送消息
    function keySend(e) {
        var theEvent = window.event || e;
        var code = theEvent.keyCode || theEvent.which;
        if (theEvent.ctrlKey && code == 13) {
            var msg = $("#msg");
            if (msg.innerHTML == "") {
                msg.focus();
                return false;
            }
            sendMsg();
        }
    }


    //发送消息
    function sendMsg() {
        //对象为空了
        if (websocket == undefined || websocket == null) {
            //alert('WebSocket connection not established, please connect.');
            alert('連線狀況異常，請重新登入');
            return;
        }
        //获取用户要发送的消息内容
        var msg = $("#msg").val();
        console.log(msg);
        if (msg == "") {
            return;
        }
        else {
            var data = {};
            data["sender_id"] = current_user_id;
            data["sender_name"] = current_user_name;
            data["receiver_id"] = current_receiver_id;
            data["receiver_name"] = current_receiver_name;
            data["text"] = msg;
            //发送消息
            websocket.send(JSON.stringify(data));
            //发送完消息，清空输入框
            $("#msg").val("");
            $('<li class="sent"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p>' + msg + '</p></li>').appendTo($('.messages ul'));
            $('.message-input input').val(null);
            //更改左側對話欄
            $('.contact.active .preview').html('<span>You: </span>' + msg);
            // $(".messages").animate({scrollTop: $(document).height()}, "fast");
            scrollToBottom();
            // $(".messages").animate({scrollTop: $(document).height()}, "fast");
        }
    }

    //关闭Websocket连接
    function closeWebsocket() {
        if (websocket != null) {
            websocket.close();
            websocket = null;
        }

    }

    //div滚动条(scrollbar)保持在最底部
    function scrollToBottom() {
        //var div = document.getElementById('chatCon');
        var div = document.getElementById('messages_div');
        console.log("height");
        console.log(div.scrollHeight);
        div.scrollTop = div.scrollHeight;

    }

    //格式化日期
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }


    function left_list_onclick(li) {
        var id = li.id.replace('left_list', '');
        console.log(id);
        window.location.href = "index?rID=" + id;
        // current_receiver_id = id;
        li.classList.add("active");
        // li.addClass("active")
    }
</script>


<script src='//production-assets.codepen.io/assets/common/stopExecutionOnTimeout-b2a7b3fe212eaa732349046d8416e00a9dec26eb7fd347590fbced3ab38af52e.js'></script>
<script>$(".messages").animate({scrollTop: $(document).height()}, "fast");

$("#profile-img").click(function () {
    $("#status-options").toggleClass("active");
});

$(".expand-button").click(function () {
    $("#profile").toggleClass("expanded");
    $("#contacts").toggleClass("expanded");
});

$("#status-options ul li").click(function () {
    $("#profile-img").removeClass();
    $("#status-online").removeClass("active");
    $("#status-away").removeClass("active");
    $("#status-busy").removeClass("active");
    $("#status-offline").removeClass("active");
    $(this).addClass("active");

    if ($("#status-online").hasClass("active")) {
        $("#profile-img").addClass("online");
    } else if ($("#status-away").hasClass("active")) {
        $("#profile-img").addClass("away");
    } else if ($("#status-busy").hasClass("active")) {
        $("#profile-img").addClass("busy");
    } else if ($("#status-offline").hasClass("active")) {
        $("#profile-img").addClass("offline");
    } else {
        $("#profile-img").removeClass();
    }
    ;

    $("#status-options").removeClass("active");
});

// function newMessage() {
//     message = $(".message-input input").val();
//     if ($.trim(message) == '') {
//         return false;
//     }
//     $('<li class="sent"><img src="http://emilcarlsson.se/assets/mikeross.png" alt="" /><p>' + message + '</p></li>').appendTo($('.messages ul'));
//     $('.message-input input').val(null);
//     $('.contact.active .preview').html('<span>You: </span>' + message);
//     $(".messages").animate({scrollTop: $(document).height()}, "fast");
// };
//
// $('.submit').click(function () {
//     newMessage();
// });

// $(window).on('keydown', function (e) {
//     if (e.which == 13) {
//         newMessage();
//         return false;
//     }
// });
//# sourceURL=pen.js
</script>
</body>
</html>
