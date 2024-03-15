package com.example.annexe7;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Ecouteur ec;
    SurfaceDessin sd;
    ConstraintLayout parent;
    int x, y, xDepart, yDepart, pointeur = -1;
    boolean start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int width = getResources().getDisplayMetrics().widthPixels * 1;
        int height = getResources().getDisplayMetrics().heightPixels * 1;
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(width, height);

        parent = findViewById(R.id.parent);
        ec = new Ecouteur();
        sd = new SurfaceDessin(this);
        sd.setLayoutParams(layoutParams);
        sd.setBackgroundResource(R.drawable.carte);
        sd.setOnTouchListener(ec);
        parent.addView(sd);
    }

    private class Ecouteur implements View.OnTouchListener
    {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            x = (int)event.getX();
            y = (int)event.getY();
            pointeur = event.getAction();

            if(pointeur == MotionEvent.ACTION_DOWN)
            {
                xDepart = x;
                yDepart = y;
                start = true;
            }
            else if(pointeur == MotionEvent.ACTION_UP)
            {
                start = false;
            }

            sd.invalidate();
            return true;
        }
    }

    private class SurfaceDessin extends View
    {
        Paint paint;
        float rectSize = 20;
        public SurfaceDessin(Context context) {
            super(context);

            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(15);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            if(start || pointeur == MotionEvent.ACTION_UP)
            {
                canvas.drawRect(xDepart - rectSize, yDepart - rectSize,
                        xDepart + rectSize, yDepart + rectSize, paint);

                canvas.drawLine(xDepart, yDepart, x, y, paint);
            }

            if(!start && pointeur == MotionEvent.ACTION_UP)
            {
                canvas.drawRect(x - rectSize, y - rectSize, x + rectSize,
                        y + rectSize, paint);
            }
        }
    }
}