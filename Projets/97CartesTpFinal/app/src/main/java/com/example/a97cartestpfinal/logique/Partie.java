package com.example.a97cartestpfinal.logique;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Partie {
    private Hashtable<TextView, Cartes> mainCartes;
    private List<Integer> carteValues = new ArrayList<>();
    private Context gameContext;
    private Piles piles;
    private final int carteMaxValue = 98;
    private int nbCartes = carteMaxValue - 1;
    private int count = 0;
    private int score = 0;
    private int turnStart, turnEnd;

    public Partie(Vector<LinearLayout> piles, Context context)
    {
        //Initialise la partie en donnant des cartes initiales au joueur et en créant les piles
        this.mainCartes = new Hashtable<>(1, 1);

        this.gameContext = context;

        for(int i = 2; i <= this.carteMaxValue; ++i)
        {
            this.carteValues.add(i);
        }
        Collections.shuffle(this.carteValues);
        System.out.println(this.carteValues.size());

        this.piles = new Piles(piles, context);
    }

    public Cartes findCard(Hashtable<TextView, Cartes> emplacementCarte, View carte)
    {
        return emplacementCarte.get(carte);
    }

    public void drawCards(Vector<LinearLayout> main, View.OnTouchListener ecot)
    {
        for(LinearLayout i : main)
        {
            if(i.getChildCount() == 0)
            {
                Cartes temp = new Cartes(this.carteValues.get(this.count), this.carteMaxValue, this.gameContext);
                ++this.count;
                this.mainCartes.put(temp.getCarte(), temp);
                temp.getCarte().setOnTouchListener(ecot);
                i.addView(temp.getCarte());
            }
        }
    }

    public void gameStart(Vector<LinearLayout> main, View.OnTouchListener ecot)
    {
        int delay = 5;
        Handler handler = new Handler();
        for(LinearLayout i : main)
        {
            handler.postDelayed(()->{
                //Make sure there are still enough values to create more cards
                if(this.count < this.carteMaxValue - 1)
                {
                    //Make sure the linear layout is empty
                    i.removeAllViews();

                    //Create the new card
                    Cartes temp = new Cartes(this.carteValues.get(this.count), this.carteMaxValue, this.gameContext);
                    this.mainCartes.put(temp.getCarte(), temp);
                    ++this.count;

                    //Add the new card to the view and associates it to a listener
                    temp.getCarte().setOnTouchListener(ecot);
                    i.addView(temp.getCarte());
                }
            },delay);
            delay += delay;
        }
    }

    public int calcNewScore(int valeurCarte, int valeurPile, boolean direction)
    {
        int defaultBonus = 5000;
        System.out.println(this.turnEnd - this.turnStart);
        if((direction && valeurPile - valeurCarte == 10)
                || (!direction && valeurCarte - valeurPile == 10))
        {
            this.score += (defaultBonus - (defaultBonus * this.nbCartes / (this.carteMaxValue))) * 20 * Math.max(1, 10 - Math.abs(this.turnEnd - this.turnStart));
        }
        else
        {
            this.score += (defaultBonus - (defaultBonus * this.nbCartes / (this.carteMaxValue))) * Math.max(1, 11 - Math.abs(valeurCarte - valeurPile)) * Math.max(1, 10 - Math.abs(this.turnEnd - this.turnStart));
        }

        return this.score;
    }

    public Piles getPiles() {
        return piles;
    }

    public Hashtable<TextView, Cartes> getMainCartes() {
        return mainCartes;
    }

    public int getNbCartes(){
        return this.nbCartes;
    }
    public void setNbCartes(int valeur) {
        this.nbCartes += valeur;
    }

    public void setTurnStart(int turnStart) {
        this.turnStart = turnStart;
    }

    public void setTurnEnd(int turnEnd) {
        this.turnEnd = turnEnd;
    }
}
