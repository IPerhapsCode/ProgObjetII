package com.example.painttp2;

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

        this.paint.setColor(ContextCompat.getColor(context, this.color));
        this.paint.setStrokeWidth(this.sizeTrace);
        this.paint.setStyle(this.style);
        this.paint.setAntiAlias(true);

    }

    public abstract void draw(float x, float y);

    public void setColor(int color) {
        this.color = color;
    }

    public void setSizeTrace(int sizeTrace) {
        this.sizeTrace = sizeTrace;
    }

    public void setStyle(Paint.Style style) {
        this.style = style;
    }

    public Paint getPaint() {
        return paint;
    }
}
