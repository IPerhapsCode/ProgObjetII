package com.example.annexe3_exercices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ActivityB extends AppCompatActivity {

    ImageView imgPlane, imgHotel, imgBeach;
    Button btnTotal;
    TextView nbBillets, nbJours, total;

    Ecouteur ec;

    int ticketCount = 0, dayCount = 0;
    double cost = 0;

    DecimalFormat decimalFormat = new DecimalFormat("0.00$");

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

        nbBillets.setText(Integer.toString(ticketCount));
        nbJours.setText(Integer.toString(dayCount));
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(v.equals(nbBillets))
            {
                ++ticketCount;
            }

            if(v.equals(nbJours))
            {
                ++dayCount;
            }

            if(v.equals(imgPlane) && ticketCount > 0)
            {
                --ticketCount;
            }

            if(v.equals(imgHotel) && dayCount > 0)
            {
                --dayCount;
            }

            if(v.equals(imgBeach))
            {
                ticketCount = 0;
                dayCount = 0;
            }

            if(v.equals(btnTotal))
            {
                BilletAvion billetAvion = new BilletAvion(ticketCount);
                HebergementHotel hebergementHotel = new HebergementHotel(dayCount);
                Commande commande = new Commande();
                commande.ajouterProduit(billetAvion);
                commande.ajouterProduit(hebergementHotel);
                cost = commande.grandTotal();
                total.setText(decimalFormat.format(cost));
            }

            nbBillets.setText(Integer.toString(ticketCount));
            nbJours.setText(Integer.toString(dayCount));
        }
    }
}