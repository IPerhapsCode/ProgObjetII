package com.example.annexe4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button button, button2, button3;

    Ecouteur ec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);

        ec = new Ecouteur();

        button.setOnClickListener(ec);
        button2.setOnClickListener(ec);
        button3.setOnClickListener(ec);
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

        }
    }
}