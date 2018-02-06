package com.ruraara.ken.e_nyumbani;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ruraara.ken.e_nyumbani.content.DataProvider;
import com.ruraara.ken.e_nyumbani.models.FeaturedProperty;
import com.ruraara.ken.e_nyumbani.sessions.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {
    Button sendBtn;
    EditText mMessageEtv;
    Context ctx = this;
    Message messageObj;
    Chat chatObj;
    private List<Message> msgList = new ArrayList<>();

    String messageProjection[] = {

            DataProvider.COL_ID,
            DataProvider.COL_CHAT_ID,
            DataProvider.COL_USER_ID,
            DataProvider.COL_MESSAGE,
            DataProvider.COL_AT,
    };

    String chatProjection[] = {

            DataProvider.COL_ID,
            DataProvider.COL_PROPERTY_ID,
            DataProvider.COL_NAME,
            DataProvider.COL_AT,
    };

    RecyclerView recyclerView;

    String p_id;
    String p_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        p_id = getIntent().getStringExtra("propertyId");
        p_title = getIntent().getStringExtra("propertyTitle");


        //Sets actionbar back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(p_title);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        Log.d("Current chats", getChats().toString());

        showMessages(getMessages(getChatId(p_id)));

        mMessageEtv = (EditText) findViewById(R.id.message);
        sendBtn = (Button) findViewById(R.id.btn_send);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                String messageText = mMessageEtv.getText().toString();
                char externalStub = messageText.charAt(messageText.length() - 1);

                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                mMessageEtv.setText("");

                if (externalStub == '*') {
                    sendMessage(messageText.trim(), true);
                } else {
                    sendMessage(messageText.trim(), false);
                }


            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendMessage(String message, boolean externalStub) {

        SessionManager sessionManager = new SessionManager(ChatActivity.this);

        ContentValues values = new ContentValues();

        values.put(DataProvider.COL_ID, 1);
        if (externalStub) {
            values.put(DataProvider.COL_USER_ID, 13);
        } else {
//            values.put(DataProvider.COL_USER_ID, sessionManager.getUserID());
            // TODO: 2/5/18 uncomment for dynamic user id

            values.put(DataProvider.COL_USER_ID, sessionManager.getUserID());
        }

        char asterisk = message.charAt(message.length() - 1);
        if (asterisk == '*') {
            StringBuilder sb = new StringBuilder(message);
            System.out.println(sb.deleteCharAt(sb.length() - 1));
            message = sb.toString();
        }

        if (!Objects.equals(getChatId(p_id), "0")) {
            values.put(DataProvider.COL_CHAT_ID, getChatId(p_id));
        }

        values.put(DataProvider.COL_MESSAGE, message);

        //Creates new chat thread
        if (!checkIfChatExists(p_id)) {
            ContentValues chatValues = new ContentValues();
            int max = 100;
            int min = 1;
            Random randomId = new Random();
            int randId = min + randomId.nextInt(max);
            chatValues.put(DataProvider.COL_ID, randId);
            chatValues.put(DataProvider.COL_PROPERTY_ID, p_id);
            chatValues.put(DataProvider.COL_NAME, p_title);

            ctx.getContentResolver().insert(DataProvider.CONTENT_URI_CHATS, chatValues);

            values.put(DataProvider.COL_CHAT_ID, randId);

        }

        Uri uri = ctx.getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);


        refreshList(getMessages(getChatId(p_id)));

    }

    private List<Message> getMessages(String chatId) {
        msgList.clear();
        String orderBy = DataProvider.COL_AT + " ASC";
        String where = DataProvider.COL_CHAT_ID + "=?";

        String[] whereArgs = new String[]{
                chatId
        };

        Cursor cursor = ctx.getContentResolver().query(DataProvider.CONTENT_URI_MESSAGES, messageProjection,
                where, whereArgs, orderBy);

        cursor.moveToFirst();
        if ((cursor.getCount() > 0)) {
            String messageId;
            do {

                messageId = String.valueOf(cursor.getInt(cursor.getColumnIndex(DataProvider.COL_ID)));
                String cId = cursor.getString(cursor.getColumnIndex(DataProvider.COL_CHAT_ID));
                String from = cursor.getString(cursor.getColumnIndex(DataProvider.COL_USER_ID));
                String message = cursor.getString(cursor.getColumnIndex(DataProvider.COL_MESSAGE));
                String at = cursor.getString(cursor.getColumnIndex(DataProvider.COL_AT));

                messageObj = new Message(messageId, cId, message, from, at);

                Log.w("Mesasge", messageId + " ," + cId + ", " + from + " ," + message + ", " + at);

                msgList.add(messageObj);

            } while (cursor.moveToNext());

        }

        Log.w("Mesasges", msgList.toString());

        return msgList;

    }

    private List<Chat> getChats() {
        List<Chat> chatList = new ArrayList<>();
        Cursor cursor = ctx.getContentResolver().query(DataProvider.CONTENT_URI_CHATS, chatProjection, null, null, null);

        cursor.moveToFirst();
        if ((cursor.getCount() > 0)) {
            String chatId;
            do {

                chatId = String.valueOf(cursor.getInt(cursor.getColumnIndex(DataProvider.COL_ID)));
                String propertyid = cursor.getString(cursor.getColumnIndex(DataProvider.COL_PROPERTY_ID));
                String name = cursor.getString(cursor.getColumnIndex(DataProvider.COL_NAME));
                String at = cursor.getString(cursor.getColumnIndex(DataProvider.COL_AT));

                chatObj = new Chat(chatId, propertyid, name, at);

                Log.w("Chat", chatId + " ," + propertyid + ", " + name + " ," + at);

                chatList.add(chatObj);

            } while (cursor.moveToNext());

        }

        Log.w("Chats", chatList.toString());

        return chatList;
    }

    private boolean checkIfChatExists(String pId) {
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

    private String getChatId(String pId) {
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

    public void showMessages(List<Message> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ChatsRecyclerViewAdapter(list, this));
        recyclerView.scrollToPosition(list.size() - 1);
    }

    public void refreshList(List<Message> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new ChatsRecyclerViewAdapter(list, this));
        recyclerView.invalidate();
        recyclerView.scrollToPosition(list.size() - 1);
    }

    public class ChatsRecyclerViewAdapter
            extends RecyclerView.Adapter<ChatsRecyclerViewAdapter.ViewHolder> {

        private final List<Message> messageArrayList;
        private final Context mContext;
        private int SELF = 100;

        public ChatsRecyclerViewAdapter(List<Message> items, Context c) {
            messageArrayList = items;
            mContext = c;
            Log.e("Adapter", "In adapter");
        }

        @Override
        public ChatsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;

            // view type is to identify where to render the chat message
            // left or right
            if (viewType == SELF) {
                // self message
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_item_self, parent, false);
            } else {
                // others message
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.chat_item_other, parent, false);
            }

            return new ChatsRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ChatsRecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.mItem = messageArrayList.get(position);
            holder.mMessageView.setText(messageArrayList.get(position).message);
        }

        @Override
        public int getItemCount() {
            return messageArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            SessionManager sessionManager = new SessionManager(ChatActivity.this);
            Message message = messageArrayList.get(position);
            /*if (message.getUserId().equals(sessionManager.getUserID())) {
                return SELF;
            }*/ // TODO: 2/5/18 uncomment for dynamic user id
            if (message.getUserId().equals(sessionManager.getUserID())) {
                return SELF;
            }

            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mMessageView;
            public Message mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mMessageView = view.findViewById(R.id.message);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mMessageView.getText() + "'";
            }
        }
    }

    public class Message {
        private String messageId = "0";
        private String chatId;
        private String message;
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

    public class Chat {
        private String chatId;
        private String propertyId;
        private String name;
        private String at;

        public Chat(String chatId, String propertyId, String name, String at) {
            this.chatId = chatId;
            this.propertyId = propertyId;
            this.name = name;
            this.at = at;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
