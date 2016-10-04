package com.joonas.mypanda;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements Observer {
    private PrintWriter toServer;
    private EditText editedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editedMessage = (EditText)findViewById(R.id.editText);

        editedMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(v.getId() == R.id.editText && !hasFocus) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    ((ScrollView) findViewById(R.id.chat_scroll)).fullScroll(View.FOCUS_DOWN);
                }
            }
        });

        /*Commmands are sent to the server via PrintWriter
        * commands are:
        *   -user
        *   -list
        *   -history
        *   -quit
         */
        try {
            Thread listenThread = new Thread(new MessageListener(SocketSingleton.getSocket().getInputStream()));
            listenThread.isDaemon();
            listenThread.start();
            /**/
            toServer = new PrintWriter(
                    /*we cannot pass sockets inputstream straight to the printwriters constructor.
                    * Make new OutputStreamWriter out from sockets inputstream*/
                    new OutputStreamWriter(SocketSingleton.getSocket().getOutputStream(), "UTF-8"),
                    //autoflush
                    true);
        } catch(IOException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                toServer.println(getString(R.string.c_user) + " " + getIntent().getStringExtra("com.joonas.MyPanda.EXTRA_MESSAGE"));
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                toServer.println(getString(R.string.c_quit));
            }
        }).start();
    }

    public void sendMessage(View view) {
        //EditText editedMessage = (EditText)findViewById(R.id.editText);
        final String insertedText = editedMessage.getText().toString();
        new Thread(new Runnable() {
            @Override
            public void run() {
                toServer.println(insertedText);
            }
        }).start();
        editedMessage.setText("");
        editedMessage.clearFocus();

    }

    /*History:\n
    * username@yyyy-MM-dd hh:mm:ss.sss:messagetext\n
    * nextmessage*/

    @Override
    public void update(String m) {
        LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        messageParams.setMargins(5, 5, 5, 5);
        final TextView message = new TextView(this);
        message.setLayoutParams(messageParams);
        message.setText(m);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayout messagesLayout = (LinearLayout) findViewById(R.id.messages_layout);
                messagesLayout.addView(message);
            }
        });
    }





    public class MessageListener implements Runnable {
        private BufferedReader in;

        public MessageListener(InputStream inFromServer) {
            try {
                in = new BufferedReader(new InputStreamReader(inFromServer, "UTF-8"));
            } catch(UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run(){
            while(true){
                try {
                    String message = in.readLine();
                    switch (message) {
                        case "> ":
                            break;
                        case "-66612341818":
                            finish();
                            break;
                        default:
                            update(message);
                            break;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

}
