package com.joonas.mypanda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendBtn = (Button)findViewById(R.id.button);
        EditText messageEdit = (EditText)findViewById(R.id.editText);


        //New intent for launching login activity
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }
}
