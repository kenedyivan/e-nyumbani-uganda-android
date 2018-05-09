package com.ruraara.ken.enyumbani;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketIOActivity extends AppCompatActivity {

    private EditText mInputMessageView;
    private EditText mToView;
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
        setContentView(R.layout.activity_socket_io);


        mSocket.emit("add user", "genymotion");
        mSocket.on("new message", onNewMessage);
        mSocket.connect();

        mInputMessageView = (EditText) findViewById(R.id.message);
        mToView = (EditText) findViewById(R.id.to);
        Button mSendMessageView = (Button) findViewById(R.id.send);

        mSendMessageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSend();
            }
        });
    }

    private void attemptSend() {
        String message = mInputMessageView.getText().toString().trim();
        String to = mToView.getText().toString().trim();
        if (TextUtils.isEmpty(message) || TextUtils.isEmpty(to)) {
            return;
        }

        String resp = "{\"to\":\""+to+"\",\"message\":\""+message+"\"}";
        mInputMessageView.setText("");
        mSocket.emit("new message", resp);
    }

    private void addMessage(String username, String message) {
        Log.d("SocketIOMsg", username + "=>" + message);
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    addMessage(username, message);*/

                    Log.d("SocketIOMsg", args[0].toString());
                }
            });
        }
    };
}
