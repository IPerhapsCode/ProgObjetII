package com.example.painttp2;

import android.content.Context;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.text.DecimalFormat;

public class Triangle extends Formes{
    private float x = -1, y = -1, a = -1, b = -1, c = -1, d = -1;
    int currentEvent = -1;
    public Triangle(Context context, int color, int sizeTrace, Paint.Style style) {
        super(context, color, sizeTrace, style);
    }

    @Override
    public void draw(float x, float y) {
        if(this.x == -1 && this.y == -1)
        {
            this.x = x;
            this.y = y;
        }
        this.a = x;
        this.b = y;
        this.c = x;
        this.d = y;
        this.calcCD(this.x, this.y, this.a, this.b);
    }

    private void calcCD(float x1, float y1, float x2, float y2)
    {
        //What a waste of my god damn time this has been
        //Will do it the same way paint does just roughly guessing what a triangle looks like
        //There could be one option for a perfect triangle and one for a rough one selected the same way as the others
        float distance =(float)Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
        float angle = (float)Math.atan2(y2 - y1, x2 - x1);
        float newAngle = (float)(angle + Math.toRadians(60));
        this.c = (float)(x1 + distance * Math.cos(newAngle));
        this.d = (float)(y1 + distance * Math.sin(newAngle));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getA() {
        return a;
    }

    public float getB() {
        return b;
    }

    public float getC() {
        return c;
    }

    public float getD() {
        return d;
    }

    public void setCurrentEvent(int currentEvent)
    {
        this.currentEvent = currentEvent;
    }
}
