package com.ruraara.ken.e_nyumbani.models;

/**
 * Created by ken on 2/6/18.
 */

public class Message {
    private String messageId = "0";
    private String chatId;
    public String message;
    private String userId;
    private String at;

    public Message(String messageId, String chatId, String message, String from, String at) {
        this.messageId = messageId;
        this.chatId = chatId;
        this.message = message;
        this.userId = from;
        this.at = at;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return message;
    }
}
