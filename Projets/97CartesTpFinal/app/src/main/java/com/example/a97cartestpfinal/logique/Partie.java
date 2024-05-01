package com.example.a97cartestpfinal.logique;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.example.a97cartestpfinal.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Partie {
    private Vector<LinearLayout> piles, main;
    private Vector<Cartes> cartes;
    private int carteMaxValue = 97;
    private List<Integer> carteValues = new ArrayList<>();
    private int count = 0;

    public Partie()
    {
        this.piles = new Vector<>(1, 1);
        this.main = new Vector<>(1, 1);
        this.cartes = new Vector<>(1, 1);

        for(int i = 1; i <= carteMaxValue; ++i)
        {
            this.carteValues.add(i);
        }
        Collections.shuffle(this.carteValues);
    }

    public void drawCard()
    {

    }

    public void gameStart(Vector<LinearLayout> main, Context context, View.OnTouchListener ecot)
    {
        int delay = 5;
        Handler handler = new Handler();
        for(LinearLayout i : main)
        {
            handler.postDelayed(()->{
                //Make sure the linear layout is empty
                i.removeAllViews();

                //Create the new card
                this.cartes.add(new Cartes(carteValues.get(this.count), this.carteMaxValue, context));
                ++this.count;

                //Add the new card to the view and associates it to a listener
                this.cartes.lastElement().getCarte().setOnTouchListener(ecot);
                i.addView(this.cartes.lastElement().getCarte());
            },delay);
            delay += delay;
        }
    }

    public Vector<Cartes> getCartes() {
        return cartes;
    }
}
