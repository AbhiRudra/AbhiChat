package com.abhi.abhishek.abhichat;

import java.util.Date;

/**
 * Created by abhishek on 16/4/18.
 */

public class ChatMessage {

    private String messagetext;
    private String messageuser;
    private long messagetime;

    public ChatMessage() {
    }

    public ChatMessage(String messagetext, String messageuser) {
        this.messagetext = messagetext;
        this.messageuser = messageuser;

        messagetime= new Date().getTime();
    }

    public String getMessagetext() {
        return messagetext;
    }

    public void setMessagetext(String messagetext) {
        this.messagetext = messagetext;
    }

    public String getMessageuser() {
        return messageuser;
    }

    public void setMessageuser(String messageuser) {
        this.messageuser = messageuser;
    }

    public long getMessagetime() {
        return messagetime;
    }

    public void setMessagetime(long messagetime) {
        this.messagetime = messagetime;
    }
}
