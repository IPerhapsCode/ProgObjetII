package com.example.painttp2.shapes;

import android.content.Context;
import android.graphics.Paint;

public class Rectangle extends Cercle {

    public Rectangle(Context context, int color, int sizeTrace, Paint.Style style) {
        super(context, color, sizeTrace, style);
    }

    //Utilise la même fonction draw que les cercles pour affiche un différent polygone
}
