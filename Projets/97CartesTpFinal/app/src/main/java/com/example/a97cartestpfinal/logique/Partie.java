package com.example.a97cartestpfinal.logique;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a97cartestpfinal.db.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Partie {
    private Hashtable<TextView, Cartes> mainCartes;
    private Hashtable<LinearLayout, Cartes> voidCartes;
    private Hashtable<Integer, Integer> savedCartes;
    private boolean savedGame;
    private List<Integer> carteValues;
    private Context gameContext;
    private Piles piles;
    private final int carteMaxValue = 97;
    private int nbCartes = carteMaxValue;
    private int count = 0;
    private int score = 0;
    private int lastScoreAddition = 0;
    private int turnStart, turnEnd, oldTurnStart;
    private String baseTime = "00:00";

    public Partie(Vector<LinearLayout> piles, Context context, boolean saved)
    {
        //Initialise la partie en donnant des cartes initiales au joueur et en créant les piles
        this.mainCartes = new Hashtable<>(1, 1);
        this.voidCartes = new Hashtable<>(1, 1);
        this.carteValues = new ArrayList<>();

        this.gameContext = context;
        this.savedGame = saved;

        this.piles = new Piles(piles, context, this.savedGame);

        if(!this.savedGame)
        {
            for(int i = 1; i <= this.carteMaxValue; ++i)
            {
                this.carteValues.add(i);
            }
            Collections.shuffle(this.carteValues);
        }
        else
        {
            Database.getInstance(context).loadGame(this);
            this.piles.loadSavedPiles(piles, this.savedCartes, context, this.carteMaxValue);
        }
    }

    public Cartes findCard(Hashtable<TextView, Cartes> emplacementCarte, View carte)
    {
        return emplacementCarte.get(carte);
    }

    public void drawCards(Vector<LinearLayout> main, View.OnTouchListener ecot)
    {
        for(LinearLayout i : main)
        {
            if(i.getChildCount() == 0 && this.count < this.carteValues.size())
            {
                System.out.println(this.count);
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
        if(!this.savedGame)
        {
            for(LinearLayout i : main)
            {
                handler.postDelayed(()->{
                    //Make sure there are still enough values to create more cards
                    if(this.count < this.carteValues.size())
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
                        System.out.println("butch");
                    }
                },delay);
                delay += delay;
            }
        }
        else
        {
            for(LinearLayout i : main)
            {
                handler.postDelayed(()->{
                    //Obtains the id of the card within the tag of the layout
                    String id = i.getTag().toString();
                    id = id.charAt(id.length() - 2) + String.valueOf(id.charAt(id.length() - 1));

                    if(this.savedCartes.containsKey(Integer.parseInt(id)))
                    {
                        //Make sure the linear layout is empty
                        i.removeAllViews();

                        //Create the new card
                        Cartes temp = new Cartes(this.savedCartes.get(Integer.parseInt(id)), this.carteMaxValue, this.gameContext);
                        this.mainCartes.put(temp.getCarte(), temp);

                        //Add the new card to the view and associates it to a listener
                        temp.getCarte().setOnTouchListener(ecot);
                        i.addView(temp.getCarte());
                        ++count;
                    }
                        }, delay);
                delay += delay;
            }
        }
    }

    public int calcNewScore(int valeurCarte, int valeurPile, boolean direction)
    {
        int defaultBonus = 5000;

        if((direction && valeurPile - valeurCarte == 10)
                || (!direction && valeurCarte - valeurPile == 10))
        {
            this.lastScoreAddition = (defaultBonus - (defaultBonus * this.nbCartes / (this.carteMaxValue + 1))) * 20 * Math.max(1, 10 - Math.abs(this.turnEnd - this.turnStart));
        }
        else
        {
            this.lastScoreAddition = (defaultBonus - (defaultBonus * this.nbCartes / (this.carteMaxValue + 1))) * Math.max(1, 11 - Math.abs(valeurCarte - valeurPile)) * Math.max(1, 10 - Math.abs(this.turnEnd - this.turnStart));
        }

        this.score += this.lastScoreAddition;

        return this.score;
    }

    public boolean gameOver(Vector<LinearLayout> pile)
    {
        for(Cartes i : this.getMainCartes().values())
        {
            for(LinearLayout j : pile)
            {
                int index = this.valeurIndex(j);
                Cartes cartePile = this.findCard(this.getPiles().getPilesCartes(), j.getChildAt(index));

                if(this.getPiles().confirmAddition(cartePile, i, j.getTag().toString().contains("asc")))
                {
                    return false;
                }
            }
        }

        return true;
    }

    //Trouve l'index approprié pour placer une nouvelle carte à partir du tag d'un linear layout
    public int valeurIndex(LinearLayout i)
    {
        if(i.getTag().toString().contains("alt"))
        {
            return 1;
        }

        return 0;
    }

    public void resetScore() {
        this.score -= this.lastScoreAddition;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void saveOldTurnStart() {
        this.oldTurnStart = this.turnStart;
    }

    public int getOldTurnStart() {
        return oldTurnStart;
    }

    public Piles getPiles() {
        return piles;
    }

    public Hashtable<TextView, Cartes> getMainCartes() {
        return mainCartes;
    }

    public Hashtable<LinearLayout, Cartes> getVoidCartes() {
        return voidCartes;
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

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public int getCarteMaxValue() {
        return carteMaxValue;
    }

    public List<Integer> getCarteValues() {
        return carteValues;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Hashtable<Integer, Integer> getSavedCartes() {
        return savedCartes;
    }

    public void setSavedCartes(Hashtable savedCartes) {
        this.savedCartes = savedCartes;
    }

    public boolean isSavedGame()
    {
        return this.savedGame;
    }
}
