package com.ruraara.ken.enyumbani.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kenedy on 9/19/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "e_nyumbani.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*db.execSQL("create table users (_id text, name text, phone text,image_name text, image blob, " +
                "saved text default '0');");*/ ///todo Uncomment this line to save user data local to the device

        db.execSQL("create table chats (_id integer, user_1 integer, user_2 integer, property_id integer, name text, last_message text, created_at datetime default current_timestamp);");

        db.execSQL("create table messages (_id integer , chat_id integer, user_id integer, message text," +
                "created_at datetime default current_timestamp);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
