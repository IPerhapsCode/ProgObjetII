package com.example.painttp2.shapes;

import android.content.Context;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.painttp2.R;

public class Efface extends DessinLibre {
    private static int sizeTraceEfface = 20;
    private Paint borderPaint;
    public Efface(Context context, int color, int sizeTrace, Paint.Style style) {
        super(context, color, sizeTrace, style);

        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(ContextCompat.getColor(context, R.color.black));
        borderPaint.setStrokeWidth(5);
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    public Paint getBorderPaint() {
        return borderPaint;
    }

    public static int getSizeTraceEfface() {
        return sizeTraceEfface;
    }

    public static void setSizeTraceEfface(int newSizeTraceEfface) {
        sizeTraceEfface = newSizeTraceEfface;
    }
}
