package com.example.a97cartestpfinal.logique;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a97cartestpfinal.MainActivity;
import com.example.a97cartestpfinal.R;

import java.util.Hashtable;
import java.util.Vector;

public class Piles {

    private Hashtable<TextView, Cartes> pilesCartes;
    private Vector<Integer> savedPiles;

    protected Piles(Vector<LinearLayout> zonePiles, Context context, boolean saved)
    {
        this.pilesCartes = new Hashtable<>(1, 1);

        //Crée les piles initiales du jeu
        if(!saved)
        {
            for(LinearLayout i : zonePiles)
            {
                int value = 0;

                if(i.getTag().toString().contains("desc"))
                {
                    value = 100;
                }

                Cartes temp;
                if(i.getTag().toString().contains("alt"))
                {
                    temp = new Cartes(value, context, R.style.cartes_pile_alt);
                    this.pilesCartes.put(temp.getCarte(), temp);
                    i.addView(temp.getCarte());
                }
                else
                {
                    temp = new Cartes(value, context, R.style.cartes_pile);
                    this.pilesCartes.put(temp.getCarte(), temp);
                    i.addView(temp.getCarte(), 0);
                }
            }
        }
    }

    public void loadSavedPiles(Vector<LinearLayout> zonePiles, Context context)
    {
        for(int i = 0; i < this.savedPiles.size(); ++i)
        {
            Cartes temp;

            if(zonePiles.get(i).getTag().toString().contains("alt"))
            {
                temp = new Cartes(this.savedPiles.get(i), context, R.style.cartes_pile_alt);
                this.pilesCartes.put(temp.getCarte(), temp);
                zonePiles.get(i).addView(temp.getCarte());
            }
            else
            {
                temp = new Cartes(this.savedPiles.get(i), context, R.style.cartes_pile);
                this.pilesCartes.put(temp.getCarte(), temp);
                zonePiles.get(i).addView(temp.getCarte(), 0);
            }
        }
    }

    public boolean addToPile(int main, LinearLayout pile, TextView carte, Partie partie)
    {
        //Trouve l'emplacement de l'ancienne carte
        int index = partie.valeurIndex(pile);

        if(this.confirmAddition(partie.findCard(this.pilesCartes, pile.getChildAt(index)),
                partie.findCard(partie.getMainCartes(), carte), pile.getTag().toString().contains("asc")))
        {
            //Applique les modifications nécessaires pour l'affichage de la carte dans le nouveau linear layout
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.7f);
            if(pile.getTag().toString().contains("alt"))
            {
                params.setMargins(MainActivity.marginsPileAlt[0],
                        MainActivity.marginsPileAlt[1],
                        MainActivity.marginsPileAlt[2],
                        MainActivity.marginsPileAlt[3]);
            }
            else
            {
                params.setMargins(MainActivity.marginsPile[0],
                        MainActivity.marginsPile[1],
                        MainActivity.marginsPile[2],
                        MainActivity.marginsPile[3]);
            }

            //Si c'est la première carte qu'on retire de la main, alors on enregistre la carte pour le bouton redo
            if(partie.getVoidCartes().size() < 2 || partie.getNbCartes() < main)
            {
                partie.getVoidCartes().put((LinearLayout) carte.getParent(), partie.findCard(partie.getMainCartes(), carte));
            }

            //Retire la carte de son linear layout original et applique les nouveau layout params
            ((LinearLayout)carte.getParent()).removeView(carte);
            carte.setOnTouchListener(null);
            carte.setLayoutParams(params);
            carte.setVisibility(View.VISIBLE);

            return true;
        }

        //Si la carte n'est pas mis dans une pile elle doit redevenir visible
        carte.setVisibility(View.VISIBLE);
        return false;
    }

    protected boolean confirmAddition(Cartes pile, Cartes main, boolean direction)
    {
        //Si la pile est ascendante
        if(direction)
        {
            return pile.getValue() < main.getValue() || pile.getValue() - main.getValue() == 10;
        }
        //Si la pile est descendante
        else
        {
            return pile.getValue() > main.getValue() || main.getValue() - pile.getValue() == 10;
        }
    }

    public Hashtable<TextView, Cartes> getPilesCartes() {
        return pilesCartes;
    }

    public Vector<Integer> getSavedPiles() {
        return savedPiles;
    }

    public void setSavedPiles(Vector<Integer> savedPiles) {
        this.savedPiles = savedPiles;
    }
}
