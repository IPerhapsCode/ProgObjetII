package com.example.a97cartestpfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Appliquer un style à une nouvelle vu:
//        ContextThemeWrapper themedContext = new ContextThemeWrapper(this, R.style.MyTextViewStyle);
//        TextView textView = new TextView(themedContext);

        //Changer la couleur du background et l'opacité de la vu
//        TextView testing = findViewById(R.id.textView8);
//        GradientDrawable bruh = (GradientDrawable) testing.getBackground();
//        bruh.setColor(Color.rgb(255, 0,0));
//        testing.setAlpha(0);
    }
}