package com.example.chat;

import java.util.List;

public class Message {
   public  static  String SEND_BY_ME="me";
   public  static String SEND_BY_BOT="bot";
    String msg,sendBy;



    public Message(String msg, String sendBy) {
        this.msg = msg;
        this.sendBy = sendBy;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }
}
