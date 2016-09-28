package com.joonas.practice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView)findViewById(R.id.hello)).setText(Integer.toString(counter));

        ((Button)findViewById(R.id.increase_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                ((TextView)findViewById(R.id.hello)).setText(Integer.toString(counter));
            }
        });
        ((Button)findViewById(R.id.decrease_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter--;
                ((TextView)findViewById(R.id.hello)).setText(Integer.toString(counter));
            }
        });
        ((Button)findViewById(R.id.reset)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = 0;
                ((TextView)findViewById(R.id.hello)).setText(Integer.toString(counter));
            }
        });
    }
}
