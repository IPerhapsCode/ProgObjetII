package com.example.annexe6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout parent;
    Paint paint;
    Canvas canvas;
    SurfaceDessin surfaceDessin;
    RectF oval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parent = findViewById(R.id.Parent);

        DisplayMetrics test =
        getResources().getDisplayMetrics();

        paint = new Paint();
        paint.setAntiAlias(true);
        canvas = new Canvas();
        oval = new RectF(360.f, 25.f, 520.f, 185.f);

        surfaceDessin = new SurfaceDessin(this);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(test.widthPixels, test.heightPixels);
        surfaceDessin.setLayoutParams(layoutParams);
        surfaceDessin.setBackgroundColor(Color.BLACK);

        parent.addView(surfaceDessin);
    }

    private class SurfaceDessin extends View
    {
        public SurfaceDessin(Context context)
        {
            super(context);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onDraw(Canvas canvas)
        {
            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(80, 100, 80, paint);

            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            canvas.drawCircle(250, 100, 80, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(getResources().getColor(R.color.yellow));
            canvas.drawArc(oval, 0.f, 120.f, true, paint);
            paint.setColor(getResources().getColor(R.color.blue));
            canvas.drawArc(oval, 120.f, 120.f, true, paint);
            paint.setColor(getResources().getColor(R.color.red));
            canvas.drawArc(oval, 240.f, 120.f, true, paint);
        }
    }
}