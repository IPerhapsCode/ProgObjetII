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
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout parent;
    SurfaceDessin surfaceDessin;
    Button button;
    Ecouteur ec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ec = new Ecouteur();

        parent = findViewById(R.id.Parent);
        button = findViewById(R.id.button);
        button.setOnClickListener(ec);

        DisplayMetrics test =
        getResources().getDisplayMetrics();

        surfaceDessin = new SurfaceDessin(this);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams((int)(test.widthPixels * 0.5),(int)(test.heightPixels * 0.1));
        surfaceDessin.setLayoutParams(layoutParams);
        surfaceDessin.setBackgroundColor(Color.BLACK);

        parent.addView(surfaceDessin);
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            surfaceDessin.setPrint();
            surfaceDessin.invalidate();
            switch((int)Math.round(Math.random() * 2))
            {
                case 1 : button.setBackgroundColor(getResources().getColor(R.color.blue)); break;
                case 2 : button.setBackgroundColor(getResources().getColor(R.color.red)); break;
                case 0 : button.setBackgroundColor(getResources().getColor(R.color.yellow)); break;
            }

        }
    }

    private class SurfaceDessin extends View
    {
        private Paint paint;
        private boolean print;
        private RectF oval;

        public SurfaceDessin(Context context)
        {
            super(context);

            print = false;
            paint = new Paint();
            paint.setAntiAlias(true);
            oval = new RectF(360.f, 25.f, 520.f, 185.f);
        }

        public void setPrint()
        {
            print = !print;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            paint.setColor(Color.GREEN);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(80, 100, 80, paint);

            paint.setColor(Color.YELLOW);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            canvas.drawCircle(250, 100, 80, paint);

            if(this.print)
            {
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
}//déplacer centre, autre méthode couleur, invalidate and redraw