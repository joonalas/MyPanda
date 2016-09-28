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

public class MainActivity extends AppCompatActivity {
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
            new Thread(new MessageListener(SocketSingleton.getSocket().getInputStream(), this)).start();
            toServer = new PrintWriter(new OutputStreamWriter(SocketSingleton.getSocket().getOutputStream(), "UTF-8"), true);
        } catch(IOException e) {
            e.printStackTrace();
        }
        toServer.println(getString(R.string.c_user) + " " + getIntent().getStringExtra("com.joonas.MyPanda.EXTRA_MESSAGE"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toServer.println(getString(R.string.c_quit));
    }

    public void sendMessage(View view) {
        //EditText editedMessage = (EditText)findViewById(R.id.editText);
        toServer.println(editedMessage.getText().toString());
        editedMessage.setText("");
        editedMessage.clearFocus();
    }

    public class MessageListener implements Runnable {
        private BufferedReader in;
        private Context mainContext;

        public MessageListener(InputStream inFromServer, Context context) {
            this.mainContext = context;
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
                            printMessage(message);
                            break;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void printMessage(String m) {
            final LinearLayout messagesLayout = (LinearLayout)findViewById(R.id.messages_layout);
            LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            messageParams.setMargins(5, 5, 5, 5);
            final TextView message = new TextView(mainContext);
            message.setLayoutParams(messageParams);
            message.setText(m);
            messagesLayout.post(new Runnable() {
                @Override
                public void run() {
                    messagesLayout.addView(message);
                }
            });
        }
    }

}
