package com.ruraara.ken.e_nyumbani.models;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ruraara.ken.e_nyumbani.content.DataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ken on 2/6/18.
 */

public class ChatRooms {
    private String chatProjection[] = {

            DataProvider.COL_ID,
            DataProvider.COL_PROPERTY_ID,
            DataProvider.COL_NAME,
            DataProvider.COL_LAST_MESSAGE,
            DataProvider.COL_AT,
    };

    private Context ctx;
    Chat chatObj;

    public ChatRooms(Context ctx){
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
                String name = cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME));
                String lastMessage = cursor.getString(cursor.getColumnIndex(DataProvider.COL_LAST_MESSAGE));
                String at = cursor.getString(cursor.getColumnIndex(DataProvider.COL_AT));

                chatObj = new Chat(chatId, propertyid, name, lastMessage,at);

                Log.w("Chat", chatId + " ," + propertyid + ", " + name + " ," + at);

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
                chatId =  String.valueOf(cursor.getInt(cursor.getColumnIndex(DataProvider.COL_ID)));
            } while (cursor.moveToNext());

            Log.d("Chat ID", chatId);
            return chatId;
        }

        return "0";
    }
}
