package com.example.a97cartestpfinal.logique;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a97cartestpfinal.MainActivity;
import com.example.a97cartestpfinal.R;


public class Cartes {
    private int couleur;
    private int value;
    private TextView carte;
    private Handler timer;

    public Cartes(int value, int maxValue, Context context)
    {
        this.value = value;
        this.couleur = Color.rgb(255, 255 - (255 * value / maxValue), 0);
        this.carte = new TextView(new ContextThemeWrapper(context, R.style.cartes_main));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(MainActivity.marginsMain[0],
                MainActivity.marginsMain[1],
                MainActivity.marginsMain[2],
                MainActivity.marginsMain[3]);
        this.carte.setLayoutParams(params);
        this.timer = new Handler();

        GradientDrawable background = (GradientDrawable) this.carte.getBackground();
        background.setColor(this.couleur);
        this.carte.setAlpha(0);
        this.carte.setText(String.valueOf(this.value));

        this.spawnAnim(2);
    }

    private void spawnAnim(int delay)
    {
        this.timer.postDelayed(()->{
            this.carte.setAlpha(this.carte.getAlpha() + 0.01f);
            if(this.carte.getAlpha() < 1)
            {
                this.spawnAnim(delay);
            }
        }, delay);
    }

    public TextView getCarte() {
        return carte;
    }
}
