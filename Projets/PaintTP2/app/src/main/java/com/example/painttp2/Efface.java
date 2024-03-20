package com.example.painttp2;

import android.content.Context;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

public class Efface extends DessinLibre{
    public static int sizeTraceEfface = 20;
    private Paint borderPaint;
    public Efface(Context context, int color, int sizeTrace, Paint.Style style) {
        super(context, color, sizeTrace, style);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(ContextCompat.getColor(context, R.color.black));
        borderPaint.setStrokeWidth(5);
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    public void setSizeTraceEfface(int sizeTraceEfface) {
        this.sizeTraceEfface = sizeTraceEfface;
    }

    public Paint getBorderPaint() {
        return borderPaint;
    }
}
