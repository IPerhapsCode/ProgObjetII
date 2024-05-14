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
    private int[] couleurValeur;
    private int value;
    private int maxValue;
    private boolean helperSelected = false;
    private TextView carte;
    private Handler timer;
    private float density;

    //Carte à jouer dans la main du joueur
    protected Cartes(int value, int maxValue, Context context, int couleur)
    {
        //Initialises the card to be placed in the players hand
        this.value = value;
        this.maxValue = maxValue;
        this.density = context.getResources().getDisplayMetrics().density;
        this.carte = new TextView(new ContextThemeWrapper(context, R.style.cartes_main));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(MainActivity.marginsMain[0],
                MainActivity.marginsMain[1],
                MainActivity.marginsMain[2],
                MainActivity.marginsMain[3]);
        this.carte.setLayoutParams(params);
        this.timer = new Handler();

        //Change the color of the cards background and it's text value
        this.setCouleur(couleur);
        this.carte.setText(String.valueOf(this.value));

        //Start the spawn animation
        this.carte.setAlpha(0);
        this.spawnAnim(1);
    }

    //Cartes utilisées pour les piles par défaut
    protected Cartes(Context context, int style, int value, int maxValue)
    {
        //Initialises the card to be placed as one of the initial piles
        this.value = value;
        this.maxValue = maxValue;
        this.density = context.getResources().getDisplayMetrics().density;
        this.carte = new TextView(new ContextThemeWrapper(context, style));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 0.7f);
        if(style == R.style.cartes_pile)
        {
            params.setMargins(MainActivity.marginsPile[0],
                    MainActivity.marginsPile[1],
                    MainActivity.marginsPile[2],
                    MainActivity.marginsPile[3]);
        }
        else if(style == R.style.cartes_pile_alt)
        {
            params.setMargins(MainActivity.marginsPileAlt[0],
                    MainActivity.marginsPileAlt[1],
                    MainActivity.marginsPileAlt[2],
                    MainActivity.marginsPileAlt[3]);
        }
        //On doit rechanger la couleur du background pour fix un bug qui reprend la dernière couleur mémoire pour toutes les piles
        this.setCouleur(-1);

        this.carte.setLayoutParams(params);
        this.carte.setText(String.valueOf(this.value));
        this.timer = new Handler();
    }

    //Spawn animation consisting of an opacity change
    public void spawnAnim(int delay)
    {
        this.timer.postDelayed(()->{
            this.carte.setAlpha(this.carte.getAlpha() + 0.01f);
            if(this.carte.getAlpha() < 1)
            {
                this.spawnAnim(delay);
            }
        }, delay);
    }

    //Lorsque le helper est actif, produit une animation sur les cartes affectées
    public void helperAnim(int delay, int color, int[] actualColor)
    {
        if(actualColor.length == 3)
        {
            //Couleurs que pourront prendre le cadre
            int[][] colors = {
                    {0, 0, 0}, //Noir
                    {255, 255, 255} //Blanc
            };

            //Assure qu'on ne sorte pas du nombre de couleur possible
            if(color >= colors.length)
            {
                color = 0;
            }

            //Change les valeurs de couleurs que prendront le cadre
            for(int i = 0; i < actualColor.length; ++i)
            {
                if(actualColor[i] < colors[color][i] && actualColor[i] < 255)
                {
                    actualColor[i] += 10;
                    if(actualColor[i] > colors[color][i])
                    {
                        actualColor[i] = colors[color][i];
                    }
                }
                else if(actualColor[i] > colors[color][i] && actualColor[i] > 0)
                {
                    actualColor[i] -= 10;
                    if(actualColor[i] < colors[color][i])
                    {
                        actualColor[i] = colors[color][i];
                    }
                }
            }

            //Si les valeurs de couleur sont égal à la couleur choisi, alors passe à la prochaine couleur
            if(actualColor[0] == colors[color][0] && actualColor[1] == colors[color][1] && actualColor[2] == colors[color][2])
            {
                ++color;
            }

            //Nécessaire que les variables soit final pour être utilisé dans une fonction lambda
            int[][] finalColor = {{color}, actualColor};
            this.timer.postDelayed(()->{
                //Change visuellement la couleur du couteur des cartes
                GradientDrawable background = (GradientDrawable) this.carte.getBackground();
                background.setStroke((int)(3 * this.density), Color.rgb(finalColor[1][0], finalColor[1][1], finalColor[1][2]));

                //Rappele cette même fonction pour continuer l'animation si jamais le helper sélectionne encore cette carte
                if(this.helperSelected)
                {
                    this.helperAnim(delay, finalColor[0][0], finalColor[1]);
                }
                //Remet de le cadre de la carte à sa couleur initiale si le helper ne sélectionne plus cette carte
                else
                {
                    background.setStroke((int)(3 * this.density), Color.rgb(0, 0, 0));
                }
            }, delay);
        }
    }

    //Change la couleur de la carte
    public void setCouleur(int couleur) {
        switch(couleur)
        {
            case 0:this.couleurValeur = new int[]{255, 255 - (255 * this.value / this.maxValue), 0}; break; //Yellow to red
            case 1:this.couleurValeur = new int[]{255 - (255 * this.value / this.maxValue), 0, 255}; break; //Pink to dark blue
            case 2:this.couleurValeur = new int[]{0, 255, 255 - (255 * this.value / this.maxValue)}; break; //Turquoise to green
            default:this.couleurValeur = new int[]{205, 199, 182}; break; //Default couleur des piles
        }
        this.couleur = Color.rgb(this.couleurValeur[0], this.couleurValeur[1], this.couleurValeur[2]);
        GradientDrawable background = (GradientDrawable) this.carte.getBackground();
        background.setColor(this.couleur);
        background.setStroke((int)(3 * this.density), Color.rgb(0, 0, 0));
    }

    //Permet d'obtenir le textview représentant cette carte
    public TextView getCarte() {
        return carte;
    }

    //Retourne la valeur de la carte
    public int getValue() {
        return value;
    }

    //Permet au helper de sélectionner la carte
    public void setHelperSelected(boolean helperSelected) {
        this.helperSelected = helperSelected;
    }
}
