package com.ruraara.ken.enyumbani.models;

/**
 * Created by ken on 2/6/18.
 */

public class Chat {
    public String chatId;
    public String propertyId;
    public String name;
    public String lastMessage;
    public String at;

    public Chat(String chatId, String propertyId, String name, String lastMessage, String at) {
        this.chatId = chatId;
        this.propertyId = propertyId;
        this.name = name;
        this.lastMessage = lastMessage;
        this.at = at;
    }

    @Override
    public String toString() {
        return name;
    }
}
