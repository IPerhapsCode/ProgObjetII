package com.example.annexe1_tp2;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    Ecouteur ec;
    SurfaceDessin sd;
    AlertDialog.Builder builder;
    AlertDialog alert;
    Path path;
    LinearLayout zoneDessin;
    Button buttonPrint, buttonErase;
    EditText xAxis, yAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ec = new Ecouteur();
        sd = new SurfaceDessin(this);

        path = new Path();
        zoneDessin = findViewById(R.id.zoneDessin);
        buttonPrint = findViewById(R.id.print);
        buttonErase = findViewById(R.id.erase);
        xAxis = findViewById(R.id.xAxis);
        yAxis = findViewById(R.id.yAxis);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(metrics.widthPixels, metrics.heightPixels);
        sd.setLayoutParams(layoutParams);
        builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("No field can be left empty!").setTitle("Error!");
        alert = builder.create();

        buttonPrint.setOnClickListener(ec);
        buttonErase.setOnClickListener(ec);
        zoneDessin.addView(sd);
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(v.equals(buttonPrint))
            {
                if(path.isEmpty() && xAxis.getText().toString().length() > 0
                        && yAxis.getText().toString().length() > 0)
                {
                    path.moveTo(Float.parseFloat(xAxis.getText().toString()),
                            Float.parseFloat(yAxis.getText().toString()));
                }
                else if(xAxis.getText().toString().length() > 0
                        && yAxis.getText().toString().length() > 0)
                {
                    path.lineTo(Float.parseFloat(xAxis.getText().toString()),
                            Float.parseFloat(yAxis.getText().toString()));
                }
                else
                {
                    xAxis.setText("0");
                    yAxis.setText("0");
                    alert.show();
                }
            }
            else if(v.equals(buttonErase))
            {
                path.reset();
            }

            sd.invalidate();
        }
    }

    private class SurfaceDessin extends View
    {
        private Paint paint;
        public SurfaceDessin(Context context) {
            super(context);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);

            if(!path.isEmpty())
            {
                canvas.drawPath(path, paint);
            }
        }
    }
}