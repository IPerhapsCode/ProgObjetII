package com.example.appcafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Ecouteur ec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ec = new Ecouteur();
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

        }
    }
}