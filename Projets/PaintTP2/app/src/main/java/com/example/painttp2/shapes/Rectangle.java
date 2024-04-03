package com.example.painttp2.shapes;

import android.content.Context;
import android.graphics.Paint;

public class Rectangle extends Cercle {

    public Rectangle(Context context, int color, int sizeTrace) {
        super(context, color, sizeTrace);
    }

    //Utilise la même fonction draw que les cercles pour affiche un différent polygone
}
