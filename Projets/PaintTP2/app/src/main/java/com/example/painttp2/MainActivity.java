package com.example.painttp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Hashtable;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    EcouteurOnTouch ecot;
    SurfaceDessin sd;
    Vector<Formes> formes;
    Vector<Button> bouttonCouleurs;
    Hashtable<String, Integer> couleurs;
    Vector<ImageView> outils;
    LinearLayout zoneCouleurs, zoneDessin, zoneOutils;

    //Variables
    int couleur = R.color.black;
    int sizeTrace = 5;
    Paint.Style style = Paint.Style.STROKE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ecot = new EcouteurOnTouch();
        sd = new SurfaceDessin(this);

        formes = new Vector<>(1, 1);
        bouttonCouleurs = new Vector<>(1, 1);
        couleurs = new Hashtable<>();
        outils = new Vector<>(1, 1);

        zoneCouleurs = findViewById(R.id.zoneCouleurs);
        zoneDessin = findViewById(R.id.zoneDessin);
        zoneOutils = findViewById(R.id.zoneOutils);

        for(int i = 0; i < zoneCouleurs.getChildCount(); ++i)
        {
            int color = 0;
            Button child = (Button)zoneCouleurs.getChildAt(i);

            bouttonCouleurs.add(child);
            bouttonCouleurs.lastElement().setOnTouchListener(ecot);

            switch(child.getTag().toString())
            {
                case "darkorange": color = R.color.darkorange; break;
                case "yellow": color = R.color.yellow; break;
                case "white": color = R.color.white; break;
                case "black": color = R.color.black; break;
                case "pink": color = R.color.pink; break;
                case "red": color = R.color.red; break;
                case "turquoise": color = R.color.turquoise; break;
                case "lightgreen": color = R.color.lightgreen; break;
            }

            couleurs.put(child.getTag().toString(), color);
        }

        for(int i = 0; i < zoneOutils.getChildCount(); ++i)
        {
            ImageView child = (ImageView)zoneOutils.getChildAt(i);

            outils.add(child);
            outils.lastElement().setOnTouchListener(ecot);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sd.setLayoutParams(params);
        zoneDessin.addView(sd);
    }

    private class EcouteurOnTouch implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(bouttonCouleurs.contains(v) && event.getAction() == MotionEvent.ACTION_DOWN)
            {
                couleur = couleurs.get(v.getTag().toString());
                System.out.println(couleur);
            }
            return true;
        }
    }

    private class SurfaceDessin extends View
    {
        public SurfaceDessin(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);


        }
    }
}