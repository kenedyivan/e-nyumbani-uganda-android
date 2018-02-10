package com.ruraara.ken.e_nyumbani.content;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by kenedy on 9/19/2015.
 */
public class DataProvider extends ContentProvider {
    public static final String DATABASE_LOG = "Database log";
    /*
    *common table columns
    */
    public static final String COL_ID = "_id";
    public static final String COL_AT = "created_at";
    /*
    *end of common table columns
    */


    /*
    *end of user column details
    */

    /*chat table details*/
    public static final String TABLE_CHATS = "chats";
    public static final String COL_USER_1 = "user_1";
    public static final String COL_USER_2 = "user_2";
    public static final String COL_PROPERTY_ID = "property_id";
    public static final String COL_NAME = "name";
    public static final String COL_LAST_MESSAGE = "last_message";
    /*
    *end of chat table details
    */

    /*
    *message table details
    */
    public static final String TABLE_MESSAGES = "messages";
    public static final String COL_CHAT_ID = "chat_id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_MESSAGE = "message";
    /*
    *end of message table details
    */

    private DbHelper dbHelper;
    //public static final Uri CONTENT_URI_USERS = Uri.parse("content://com.ruraara.ken.e_nyumbani.content.dataprovider/users");
    public static final Uri CONTENT_URI_CHATS = Uri.parse("content://com.ruraara.ken.e_nyumbani.content.dataprovider/chats");
    public static final Uri CONTENT_URI_MESSAGES = Uri.parse("content://com.ruraara.ken.e_nyumbani.content.dataprovider/messages");


    //private static final int USERS_ALLROWS = 1;
    //private static final int USERS_SINGLE_ROW = 2;
    private static final int CHATS_ALLROWS = 3;
    private static final int CHATS_SINGLE_ROW = 4;
    private static final int MESSAGES_ALLROWS = 5;
    private static final int MESSAGES_SINGLE_ROW = 6;


    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //uriMatcher.addURI("com.ruraara.ken.e_nyumbani.content.dataprovider", "users", USERS_ALLROWS);
        //uriMatcher.addURI("com.ruraara.ken.e_nyumbani.content.dataprovider", "users/#", USERS_SINGLE_ROW);
        uriMatcher.addURI("com.ruraara.ken.e_nyumbani.content.dataprovider", "chats", CHATS_ALLROWS);
        uriMatcher.addURI("com.ruraara.ken.e_nyumbani.content.dataprovider", "chats/#", CHATS_SINGLE_ROW);
        uriMatcher.addURI("com.ruraara.ken.e_nyumbani.content.dataprovider", "messages", MESSAGES_ALLROWS);
        uriMatcher.addURI("com.ruraara.ken.e_nyumbani.content.dataprovider", "messages/#", MESSAGES_SINGLE_ROW);

    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALLROWS:
            //case USERS_ALLROWS:
            case CHATS_ALLROWS:
                qb.setTables(getTableName(uri));
                break;

            case MESSAGES_SINGLE_ROW:
            //case USERS_SINGLE_ROW:
            case CHATS_SINGLE_ROW:
                qb.setTables(getTableName(uri));
                qb.appendWhere("_id = " + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALLROWS:
                id = db.insertOrThrow(TABLE_MESSAGES, null, values);
                /*if (values.get(COL_TO) == null) {
                    db.execSQL("update users set count=count+1 where chat_id = ?", new Object[]{values.get(COL_FROM)});
                    getContext().getContentResolver().notifyChange(CONTENT_URI_USERS, null);
                }*/
                Log.d(DATABASE_LOG, "Inserting message data");
                break;

            /*case USERS_ALLROWS:
                id = db.insertOrThrow(TABLE_USERS, null, values);
                Log.d(DATABASE_LOG, "Inserting user data");
                break;*/

            case CHATS_ALLROWS:
                id = db.insertOrThrow(TABLE_CHATS, null, values);
                Log.d(DATABASE_LOG, "Inserting chat data");
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Uri insertUri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(insertUri, null);
        return insertUri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int count;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALLROWS:
            //case USERS_ALLROWS:
            case CHATS_ALLROWS:
                count = db.update(getTableName(uri), values, selection, selectionArgs);
                break;

            case MESSAGES_SINGLE_ROW:
            //case USERS_SINGLE_ROW:
            case CHATS_SINGLE_ROW:
                count = db.update(getTableName(uri), values, "_id = ?", new String[]{uri.getLastPathSegment()});
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int count;
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALLROWS:
            //case USERS_ALLROWS:
            case CHATS_ALLROWS:
                count = db.delete(getTableName(uri), selection, selectionArgs);
                break;

            case MESSAGES_SINGLE_ROW:
            //case USERS_SINGLE_ROW:
            case CHATS_SINGLE_ROW:
                count = db.delete(getTableName(uri), "_id = ?", new String[]{uri.getLastPathSegment()});
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private String getTableName(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MESSAGES_ALLROWS:
            case MESSAGES_SINGLE_ROW:
                return TABLE_MESSAGES;

            /*case USERS_ALLROWS:
            case USERS_SINGLE_ROW:
                return TABLE_USERS;*/

            case CHATS_ALLROWS:
            case CHATS_SINGLE_ROW:
                return TABLE_CHATS;
        }
        return null;
    }


}
