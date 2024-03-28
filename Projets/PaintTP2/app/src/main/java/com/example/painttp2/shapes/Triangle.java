package com.example.painttp2.shapes;

import android.content.Context;
import android.graphics.Paint;

public class Triangle extends Formes {
    private float x = -1, y = -1, a = -1, b = -1, c = -1, d = -1;
    private int styleTriangle;
    public Triangle(Context context, int color, int sizeTrace, Paint.Style style, int styleTriangle) {
        super(context, color, sizeTrace, style);
        this.getPaint().setStrokeCap(Paint.Cap.ROUND);
        this.styleTriangle = styleTriangle;
    }

    //Permet de dessiner les différents triangles
    @Override
    public void draw(float x, float y) {
        if(this.x == -1 && this.y == -1)
        {
            this.x = x;
            this.y = y;

            if(this.styleTriangle == 1)
            {
                this.b = y;
                this.c = x;
            }
            if(this.styleTriangle == 2)
            {
                this.b = y;
            }
            else if(this.styleTriangle == 3)
            {
                this.a = x;
            }
        }
        if(this.styleTriangle == 0)
        {
            this.a = x;
            this.b = y;

            float distance =(float)Math.sqrt(Math.pow((this.a - this.x), 2) + Math.pow((this.b - this.y), 2));
            float angle = (float)Math.atan2(this.b - this.y, this.a - this.x);
            float newAngle = (float)(angle + Math.toRadians(60));
            this.c = (float)(this.x + distance * Math.cos(newAngle));
            this.d = (float)(this.y + distance * Math.sin(newAngle));
        }
        else if(this.styleTriangle == 1)
        {
            this.a = x;
            this.d = y;
        }
        else if(this.styleTriangle == 2)
        {
            this.a = x;
            this.d = y;
            this.c = (this.x + this.a) / 2;
        }
        else if(this.styleTriangle == 3)
        {
            this.b = y;
            this.c = x;
            this.d = (this.y + this.b) / 2;
        }
    }

    //Retourne les différentes valeurs des points d'un triangle
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
}
