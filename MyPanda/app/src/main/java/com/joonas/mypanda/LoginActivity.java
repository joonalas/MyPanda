package com.joonas.mypanda;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        connect();
        checkConnection();
    }

    public void checkConnection() {
        if(SocketSingleton.getSocket()!=null) {
            finish();
        } else {
            RelativeLayout loginLayout = (RelativeLayout)findViewById(R.id.activity_login);
            View loginLayoutInflated = LayoutInflater.from(this).inflate(R.layout.layout_no_connection, loginLayout, false);
            loginLayout.addView(loginLayoutInflated);
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
                    byte[] addressBytes = {(byte) (87), (byte) (95), (byte) (52), (byte) (167)};
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
