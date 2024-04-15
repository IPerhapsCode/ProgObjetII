package com.example.pratiqueexam2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Ecouteur ec;
    SurfaceDessin sd;

    LinearLayout parent;

    float x, y;

    Vector<Path> drawings = new Vector<>(1, 1);

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ec = new Ecouteur();
        sd = new SurfaceDessin(this);

        parent = findViewById(R.id.parent);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);
        sd.setLayoutParams(layoutParams);
        sd.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        sd.setOnTouchListener(ec);

        parent.addView(sd);
    }

    private class Ecouteur implements View.OnTouchListener
    {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            x = event.getX();
            y = event.getY();

            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                drawings.add(new Path());
                drawings.lastElement().moveTo(x, y);
            }
            else if(event.getAction() == MotionEvent.ACTION_MOVE)
            {
                drawings.lastElement().lineTo(x, y);
            }

            sd.invalidate();

            return true;
        }
    }

    private class SurfaceDessin extends View
    {
        Paint paint;

        public SurfaceDessin(Context context) {
            super(context);

            this.paint = new Paint();
            this.paint.setAntiAlias(true);
            this.paint.setStrokeWidth(10);
            this.paint.setStyle(Paint.Style.STROKE);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            this.paint.setColor(ContextCompat.getColor(MainActivity.this, R.color.teal_200));
            canvas.drawCircle(50, 50, 30, this.paint);

            this.paint.setColor(ContextCompat.getColor(MainActivity.this, R.color.black));
            canvas.drawRect(100, 20, 200, 80, this.paint);

            for(Path i : drawings)
            {
                canvas.drawPath(i, this.paint);
            }
        }
    }
}