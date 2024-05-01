package com.example.a97cartestpfinal.logique;

import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Vector;

public class Piles {
    private TextView[] piles;

    protected Piles(Vector<LinearLayout> zonePiles)
    {
        this.piles = new TextView[zonePiles.size()];
        for(int i  = 0; i < zonePiles.size(); ++i)
        {
            for(int j = 0; j < zonePiles.get(i).getChildCount(); ++j)
            {
                if(zonePiles.get(i).getChildAt(j) instanceof TextView)
                {
                    piles[i] = (TextView) zonePiles.get(i).getChildAt(j);
                }
            }
        }
    }

    public void addToPile(LinearLayout pile, TextView carte)
    {
        for(int i = 0; i < pile.getChildCount(); ++i)
        {
            if(pile.getChildAt(i) instanceof TextView)
            {
                pile.removeView(pile.getChildAt(i));
                break;
            }
        }
        
    }
}
