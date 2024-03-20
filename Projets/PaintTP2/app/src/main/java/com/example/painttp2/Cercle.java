package com.example.painttp2;

import android.content.Context;
import android.graphics.Paint;

public class Cercle extends Formes{
    private float xDepart = -1, yDepart = -1, x = -1, y = -1;

    public Cercle(Context context, int color, int sizeTrace, Paint.Style style) {
        super(context, color, sizeTrace, style);
    }

    @Override
    public void draw(float x, float y) {
        if(this.xDepart == -1 && this.yDepart == -1)
        {
            this.xDepart = x;
            this.yDepart = y;
        }
        this.x = x;
        this.y = y;
    }

    public float getxDepart() {
        return xDepart;
    }

    public float getyDepart() {
        return yDepart;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
