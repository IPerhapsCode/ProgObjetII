package com.example.painttp2.shapes;

import android.content.Context;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public abstract class Formes {
    private Paint paint;
    private int color;
    private int sizeTrace;
    private Paint.Style style;

    public Formes(Context context, int color, int sizeTrace, Paint.Style style)
    {
        this.paint = new Paint();
        this.color = color;
        this.sizeTrace = sizeTrace;
        this.style = style;

        try
        {
            //Permet d'assigner une couleur à partir de nos couleurs prédéfinies
            this.paint.setColor(ContextCompat.getColor(context, this.color));
        }
        catch(Exception e)
        {
            //Permet d'assigner une couleur obtenu à travers la pipette ou encore la pallette de couleur
            this.paint.setColor(this.color);
        }
        this.paint.setStrokeWidth(this.sizeTrace);
        this.paint.setStyle(this.style);
        this.paint.setAntiAlias(false);
    }

    //Fonction permettant de dessinner variant d'une forme à l'autre
    public abstract void draw(float x, float y);

//    //Permet de changer la couleur de notre peinture
//    public void setColor(int color) {
//        this.color = color;
//    }

    public void setStyle(Paint.Style style) {
        this.style = style;
    }

    public Paint getPaint() {
        return paint;
    }
}
