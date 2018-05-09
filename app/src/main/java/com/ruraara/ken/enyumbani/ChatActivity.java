package com.ruraara.ken.enyumbani;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.ruraara.ken.enyumbani.content.DataProvider;
import com.ruraara.ken.enyumbani.models.ChatRooms;
import com.ruraara.ken.enyumbani.models.Message;
import com.ruraara.ken.enyumbani.sessions.SessionManager;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity {
    SessionManager sessionManager;
    Button sendBtn;
    EditText mMessageEtv;
    Context ctx = this;
    Message messageObj;
    private List<Message> msgList = new ArrayList<>();

    String messageProjection[] = {

            DataProvider.COL_ID,
            DataProvider.COL_CHAT_ID,
            DataProvider.COL_USER_ID,
            DataProvider.COL_MESSAGE,
            DataProvider.COL_AT,
    };


    RecyclerView recyclerView;

    String agent_id;
    String p_id;
    String p_title;
    ChatRooms chatRooms;

    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://10.0.3.2:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        sessionManager = new SessionManager(ChatActivity.this);

        p_id = getIntent().getStringExtra("propertyId");
        agent_id = getIntent().getStringExtra("agentId");
        p_title = getIntent().getStringExtra("propertyTitle");

        mSocket.emit("disconn",sessionManager.getUserID());
        mSocket.emit("add user", sessionManager.getUserID());
        mSocket.on("new message", onNewMessage);
        mSocket.connect();

        chatRooms = new ChatRooms(ctx);


        //Sets actionbar back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setTitle(p_title);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        showMessages(getMessages(chatRooms.getChatId(p_id)));

        mMessageEtv = (EditText) findViewById(R.id.message);
        sendBtn = (Button) findViewById(R.id.btn_send);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                String messageText = mMessageEtv.getText().toString();

                if (TextUtils.isEmpty(messageText)) {
                    return;
                }

                mMessageEtv.setText("");

                sendMessage(messageText.trim());


            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendMessage(String message) {

        ContentValues values = new ContentValues();

        values.put(DataProvider.COL_ID, 1);
        values.put(DataProvider.COL_USER_ID, sessionManager.getUserID());

        if (!Objects.equals(chatRooms.getChatId(p_id), "0")) {
            values.put(DataProvider.COL_CHAT_ID, chatRooms.getChatId(p_id));
        }

        values.put(DataProvider.COL_MESSAGE, message);

        //Creates new chat thread
        if (!chatRooms.checkIfChatExists(p_id)) {
            ContentValues chatValues = new ContentValues();
            int max = 1000;
            int min = 1;
            Random randomId = new Random();
            int randId = min + randomId.nextInt(max);
            chatValues.put(DataProvider.COL_ID, randId);
            chatValues.put(DataProvider.COL_USER_1, sessionManager.getUserID());
            chatValues.put(DataProvider.COL_USER_2, agent_id);
            chatValues.put(DataProvider.COL_PROPERTY_ID, p_id);
            chatValues.put(DataProvider.COL_NAME, p_title);
            chatValues.put(DataProvider.COL_LAST_MESSAGE, message);

            ctx.getContentResolver().insert(DataProvider.CONTENT_URI_CHATS, chatValues);

            values.put(DataProvider.COL_CHAT_ID, randId);

        } else {
            Log.d("There", "Already there");
        }

        ctx.getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);

        transportMessage(getMessages(chatRooms.getChatId(p_id)), message);


    }

    private void updateLastMessage(String lastMessage, String chatId) {
        ContentValues chatValues = new ContentValues();
        chatValues.put(DataProvider.COL_LAST_MESSAGE, lastMessage);

        String where = DataProvider.COL_ID + "=?";

        String[] whereArgs = new String[]{
                chatId
        };

        ctx.getContentResolver().update(DataProvider.CONTENT_URI_CHATS, chatValues,
                where, whereArgs);

    }

    private List<Message> getMessages(String chatId) {
        Log.d("ChatId", chatId);
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

    public void showMessages(List<Message> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MessagesRecyclerViewAdapter(list, this));
        recyclerView.scrollToPosition(list.size() - 1);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void transportMessage(List<Message> list, String lm) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MessagesRecyclerViewAdapter(list, this));
        recyclerView.invalidate();
        recyclerView.scrollToPosition(list.size() - 1);
        updateLastMessage(lm, chatRooms.getChatId(p_id));
        String chatId = chatRooms.getChatId(p_id);
        String to = null;
        if (!Objects.equals(chatRooms.getUser1(chatId), sessionManager.getUserID())) {
            to = chatRooms.getUser1(chatId);
        } else if (!Objects.equals(chatRooms.getUser2(chatId), sessionManager.getUserID())) {
            to = chatRooms.getUser2(chatId);
        }
        attemptSend(sessionManager.getUserID(), to, p_id, chatId, lm);
    }

    private void attemptSend(String from, String to, String propertyId, String chatId, String message) {

        String req = "{\"from\":\"" + from + "\",\"to\":\"" + to + "\",\"property_id\":\"" +
                propertyId + "\",\"chat_id\":\"" + chatId + "\", \"title\":\"" + p_title + "\", \"message\":\"" + message + "\"}";

        mSocket.emit("new message", req);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void parseReceivedMessage(Object[] args) {

        JSONObject data = (JSONObject) args[0];
        String from = null;
        String to = null;
        String propertyId = null;
        String chatId = null;
        String title = null;
        String message = null;

        try {
            from = data.getString("from");
            to = data.getString("to");
            propertyId = data.getString("property_id");
            chatId = data.getString("chat_id");
            title = data.getString("title");
            message = data.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        storeReceivedMessage(from, to, propertyId, chatId, title, message);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void storeReceivedMessage(String from, String to, final String propertyId, String chatId, String title, final String message) {
        ContentValues values = new ContentValues();

        values.put(DataProvider.COL_ID, 1);
        values.put(DataProvider.COL_USER_ID, from);

        if (!Objects.equals(chatRooms.getChatId(propertyId), "0")) {
            values.put(DataProvider.COL_CHAT_ID, chatRooms.getChatId(propertyId));
        }

        values.put(DataProvider.COL_MESSAGE, message);

        //Creates new chat thread
        if (!chatRooms.checkIfChatExists(propertyId)) {
            ContentValues chatValues = new ContentValues();
            chatValues.put(DataProvider.COL_ID, chatId);
            chatValues.put(DataProvider.COL_USER_1, from);
            chatValues.put(DataProvider.COL_USER_2, to);
            chatValues.put(DataProvider.COL_PROPERTY_ID, propertyId);
            chatValues.put(DataProvider.COL_NAME, title);
            chatValues.put(DataProvider.COL_LAST_MESSAGE, message);

            ctx.getContentResolver().insert(DataProvider.CONTENT_URI_CHATS, chatValues);

            values.put(DataProvider.COL_CHAT_ID, chatId);

        } else {
            Log.d("There", "Already there");
        }

        ctx.getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshMessageList(getMessages(chatRooms.getChatId(propertyId)), propertyId, message);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void refreshMessageList(List<Message> list, String propertyId, String lm) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ctx);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new MessagesRecyclerViewAdapter(list, this));
        recyclerView.invalidate();
        recyclerView.scrollToPosition(list.size() - 1);
        updateLastMessage(lm, chatRooms.getChatId(propertyId));
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void call(final Object... args) {
            parseReceivedMessage(args);
        }
    };


    public class MessagesRecyclerViewAdapter
            extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder> {

        private final List<Message> messageArrayList;
        private final Context mContext;
        private int SELF = 100;

        public MessagesRecyclerViewAdapter(List<Message> items, Context c) {
            messageArrayList = items;
            mContext = c;
            Log.e("Adapter", "In adapter");
        }

        @Override
        public MessagesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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

            return new MessagesRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MessagesRecyclerViewAdapter.ViewHolder holder, final int position) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.property_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(ChatActivity.this, TopSettingsActivity.class);
            startActivity(i);
            return true;
        }

        if (id == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            /*Intent intent = NavUtils.getParentActivityIntent(this);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NavUtils.navigateUpTo(this, intent);*/
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*Log.d("Destroying", "Socket destroyed");
        mSocket.emit("disconnect");*/

        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }

    @Override
    protected void onPause() {
       super.onPause();
        /*Log.d("Destroying", "Socket destroyed");
        mSocket.emit("disconnect");*/

        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }
}
