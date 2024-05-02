package com.example.a97cartestpfinal.logique;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a97cartestpfinal.MainActivity;
import com.example.a97cartestpfinal.R;

import java.util.Vector;

public class Piles {

    protected Piles(Vector<LinearLayout> zonePiles, Context context)
    {
        //Crée les piles initiales du jeu
        for(LinearLayout i : zonePiles)
        {
            int value = 1;

            if(i.getTag().toString().contains("desc"))
            {
                value = 100;
            }

            if(i.getTag().toString().matches(".*alt"))
            {
                Cartes carte = new Cartes(value, context, R.style.cartes_pile_alt);
                i.addView(carte.getCarte());
            }
            else
            {
                Cartes carte = new Cartes(value, context, R.style.cartes_pile);
                i.addView(carte.getCarte(), 0);
            }
        }
    }

    public void addToPile(LinearLayout pile, TextView carte)
    {
        //Trouve l'emplacement de l'ancienne carte et la retire de la pile
        int index = 0;
        for(int i = 0; i < pile.getChildCount(); ++i)
        {
            if(pile.getChildAt(i) instanceof TextView)
            {
                index = i;
                pile.removeView(pile.getChildAt(i));
                break;
            }
        }

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

        //Dépalce la carte d'un linear layout à l'autre
        ((LinearLayout)carte.getParent()).removeView(carte);
        carte.setOnTouchListener(null);
        carte.setLayoutParams(params);
        carte.setVisibility(View.VISIBLE);
        pile.addView(carte, index);
    }
}
