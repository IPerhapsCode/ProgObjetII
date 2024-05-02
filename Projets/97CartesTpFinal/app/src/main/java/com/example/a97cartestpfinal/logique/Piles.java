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

    protected Piles(Vector<LinearLayout> zonePiles, Context context)
    {
        this.pilesCartes = new Hashtable<>(1, 1);

        //Crée les piles initiales du jeu
        for(LinearLayout i : zonePiles)
        {
            int value = 1;

            if(i.getTag().toString().contains("desc"))
            {
                value = 100;
            }

            Cartes temp;
            if(i.getTag().toString().matches(".*alt"))
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

    public void addToPile(View.OnTouchListener ecot, Vector<LinearLayout> main, LinearLayout pile, TextView carte, Partie partie)
    {
        //Trouve l'emplacement de l'ancienne carte
        int index = 0;
        for(int i = 0; i < pile.getChildCount(); ++i)
        {
            if(pile.getChildAt(i) instanceof TextView)
            {
                index = i;
                break;
            }
        }

        if(this.confirmAddition(partie.findCard(this.pilesCartes, pile.getChildAt(index)),
                partie.findCard(partie.getMainCartes(), carte), pile.getTag().toString().contains("asc")))
        {
            //Applique les modifications nécessaires pour l'affichage de la carte dans le nouveau linear layout
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.7f);
            if(pile.getTag().toString().matches(".*alt"))
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

            //Déplace la carte d'un linear layout à l'autre
            ((LinearLayout)carte.getParent()).removeView(carte);
            carte.setOnTouchListener(null);
            carte.setLayoutParams(params);
            this.pilesCartes.remove(pile.getChildAt(index));
            this.pilesCartes.put(carte, partie.findCard(partie.getMainCartes(), carte));
            partie.getMainCartes().remove(carte);
            pile.removeView(pile.getChildAt(index));
            pile.addView(carte, index);

            //Fait piger le joueur s'il leur manque au moins deux cartes
            if(partie.getMainCartes().size() <= main.size() - 2)
            {
                partie.drawCards(main, ecot);
            }
        }

        //Que la carte soit mis dans la pile ou non, elle redevient visible
        carte.setVisibility(View.VISIBLE);
    }

    public boolean confirmAddition(Cartes pile, Cartes main, boolean direction)
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
}
