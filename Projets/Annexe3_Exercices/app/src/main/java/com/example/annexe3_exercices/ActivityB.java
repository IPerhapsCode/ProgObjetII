package com.example.annexe3_exercices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ActivityB extends AppCompatActivity {

    ImageView imgPlane, imgHotel, imgBeach;
    Button btnTotal;
    TextView nbBillets, nbJours, total;

    Ecouteur ec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        imgPlane = findViewById(R.id.imgPlane);
        imgHotel = findViewById(R.id.imgHotel);
        imgBeach = findViewById(R.id.imgBeach);
        btnTotal = findViewById(R.id.btnTotal);
        nbBillets = findViewById(R.id.nbBillets);
        nbJours = findViewById(R.id.nbJours);
        total = findViewById(R.id.total);

        ec = new Ecouteur();

        imgPlane.setOnClickListener(ec);
        imgHotel.setOnClickListener(ec);
        imgBeach.setOnClickListener(ec);
        btnTotal.setOnClickListener(ec);
        nbBillets.setOnClickListener(ec);
        nbJours.setOnClickListener(ec);
        total.setOnClickListener(ec);
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

        }
    }
}