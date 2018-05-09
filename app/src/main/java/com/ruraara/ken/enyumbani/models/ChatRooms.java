package com.ruraara.ken.enyumbani.models;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ruraara.ken.enyumbani.content.DataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ken on 2/6/18.
 */

public class ChatRooms {
    private String chatProjection[] = {

            DataProvider.COL_ID,
            DataProvider.COL_USER_1,
            DataProvider.COL_USER_2,
            DataProvider.COL_PROPERTY_ID,
            DataProvider.COL_NAME,
            DataProvider.COL_LAST_MESSAGE,
            DataProvider.COL_AT,
    };

    private Context ctx;
    Chat chatObj;

    public ChatRooms(Context ctx) {
        this.ctx = ctx;
    }

    public List<Chat> getChats() {
        List<Chat> chatList = new ArrayList<>();
        Cursor cursor = ctx.getContentResolver().query(DataProvider.CONTENT_URI_CHATS, chatProjection, null, null, null);

        cursor.moveToFirst();
        if ((cursor.getCount() > 0)) {
            String chatId;
            do {

                chatId = String.valueOf(cursor.getInt(cursor.getColumnIndex(DataProvider.COL_ID)));
                String propertyid = cursor.getString(cursor.getColumnIndex(DataProvider.COL_PROPERTY_ID));
                String user1 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_USER_1));
                String user2 = cursor.getString(cursor.getColumnIndex(DataProvider.COL_USER_2));
                String name = cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME));
                String lastMessage = cursor.getString(cursor.getColumnIndex(DataProvider.COL_LAST_MESSAGE));
                String at = cursor.getString(cursor.getColumnIndex(DataProvider.COL_AT));

                chatObj = new Chat(chatId, propertyid, name, lastMessage, at);

                Log.w("Chat Rooms", "chat: "+chatId + " ,u1: " + user1 + " ,u2: " + user2
                        + ", property: " + propertyid + ", name: " + name + " ,at: " + at);

                chatList.add(chatObj);

            } while (cursor.moveToNext());

        }

        Log.w("Chats", chatList.toString());

        return chatList;
    }

    public boolean checkIfChatExists(String pId) {
        String where = DataProvider.COL_PROPERTY_ID + "=?";

        String[] whereArgs = new String[]{
                pId
        };

        Cursor cursor = ctx.getContentResolver().query(DataProvider.CONTENT_URI_CHATS, chatProjection, where, whereArgs, null);

        cursor.moveToFirst();
        if ((cursor.getCount() > 0)) {
            return true;

        }

        return false;
    }

    public String getChatId(String pId) {
        Log.d("Property ID", pId);
        String where = DataProvider.COL_PROPERTY_ID + "=?";

        String[] whereArgs = new String[]{
                pId
        };

        Cursor cursor = ctx.getContentResolver().query(DataProvider.CONTENT_URI_CHATS, chatProjection, where, whereArgs, null);

        cursor.moveToFirst();
        if ((cursor.getCount() > 0)) {
            String chatId;
            do {
                chatId = String.valueOf(cursor.getInt(cursor.getColumnIndex(DataProvider.COL_ID)));
            } while (cursor.moveToNext());

            Log.d("Chat ID", chatId);
            return chatId;
        }

        return "0";
    }

    public String getUser1(String cId) {
        Log.d("Chat ID", cId);
        String where = DataProvider.COL_ID + "=?";

        String[] whereArgs = new String[]{
                cId
        };

        Cursor cursor = ctx.getContentResolver().query(DataProvider.CONTENT_URI_CHATS, chatProjection, where, whereArgs, null);

        cursor.moveToFirst();
        if ((cursor.getCount() > 0)) {
            String user1;
            do {
                user1 = String.valueOf(cursor.getInt(cursor.getColumnIndex(DataProvider.COL_USER_1)));
            } while (cursor.moveToNext());

            Log.d("Agent ID", user1);
            return user1;
        }

        return "0";
    }

    public String getUser2(String cId) {
        Log.d("Chat ID", cId);
        String where = DataProvider.COL_ID + "=?";

        String[] whereArgs = new String[]{
                cId
        };

        Cursor cursor = ctx.getContentResolver().query(DataProvider.CONTENT_URI_CHATS, chatProjection, where, whereArgs, null);

        cursor.moveToFirst();
        if ((cursor.getCount() > 0)) {
            String user2;
            do {
                user2 = String.valueOf(cursor.getInt(cursor.getColumnIndex(DataProvider.COL_USER_2)));
            } while (cursor.moveToNext());

            Log.d("Agent ID", user2);
            return user2;
        }

        return "0";
    }

    public String getChatTitle(String cId) {
        Log.d("Chat ID", cId);
        String where = DataProvider.COL_NAME + "=?";

        String[] whereArgs = new String[]{
                cId
        };

        Cursor cursor = ctx.getContentResolver().query(DataProvider.CONTENT_URI_CHATS, chatProjection, where, whereArgs, null);

        cursor.moveToFirst();
        if ((cursor.getCount() > 0)) {
            String chatName;
            do {
                chatName = String.valueOf(cursor.getInt(cursor.getColumnIndex(DataProvider.COL_NAME)));
            } while (cursor.moveToNext());

            Log.d("Agent ID", chatName);
            return chatName;
        }

        return "0";
    }
}
