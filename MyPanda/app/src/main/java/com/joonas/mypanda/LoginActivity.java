package com.joonas.mypanda;

import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/*this is the launcher activity which handles signing in*/

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connect();
        checkConnection();
    }

    public void login(View view) {
        /*get the text in edittextview and if it's */
        EditText setUsername = (EditText)findViewById(R.id.setUsername);
        if(!setUsername.getText().toString().isEmpty()){
            Intent startMain = new Intent(this, MainActivity.class);
            startMain.putExtra("com.joonas.MyPanda.EXTRA_MESSAGE", setUsername.getText().toString());
            startActivity(startMain);
            finish();
        }
    }

    public void checkConnection() {
        RelativeLayout mainLayout = (RelativeLayout)findViewById(R.id.activity_login);
        if(SocketSingleton.getSocket()!=null) {
            View loginLayout = LayoutInflater.from(this).inflate(R.layout.layout_login, mainLayout, false);
            mainLayout.addView(loginLayout);
        } else {
            View errorLayout = LayoutInflater.from(this).inflate(R.layout.layout_no_connection, mainLayout, false);
            mainLayout.addView(errorLayout);
        }
    }

    public void reconnect(View view) {
        setContentView(R.layout.activity_login);
        connect();
        checkConnection();
    }

    private void connect() {
        Thread connectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] addressBytes = {(byte) (10), (byte) (112), (byte) (199), (byte) (211)};
                    InetAddress address = InetAddress.getByAddress(addressBytes);
                    SocketSingleton.setSocket(new Socket(address, 52828));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        connectThread.start();
        try {
            connectThread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
