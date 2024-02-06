package com.example.annexe3_exercices;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {


    Ecouteur ec;
    TextView targetQuantity;
    TextView currentQuantity;
    ProgressBar progressBar;
    ImageView imgBidon;
    ImageView imgBottle;
    ImageView imgGlass;

    int target = 2000;
    int current = 0;
    int bidon = 1500;
    int bottle = 330;
    int glass = 150;

    String quantityTypeML = "ml";
    String quantityTypeL = "l";

    String congratulation = "You did it!";
    int duration = Toast.LENGTH_SHORT;
    Toast toast;
    boolean doOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        targetQuantity = findViewById(R.id.targetQuantity);
        currentQuantity = findViewById(R.id.currentQuantity);
        progressBar = findViewById(R.id.progressBar);
        imgBidon = findViewById(R.id.imgBidon);
        imgBottle = findViewById(R.id.imgBottle);
        imgGlass = findViewById(R.id.imgGlass);

        ec = new Ecouteur();

        imgBidon.setOnClickListener(ec);
        imgBottle.setOnClickListener(ec);
        imgGlass.setOnClickListener(ec);

        progressBar.setMax(target);
        toast = Toast.makeText(MainActivity.this /* MyActivity */, congratulation, duration);
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(v.equals(imgBidon))
            {
                current += bidon;
            }

            if(v.equals(imgBottle))
            {
                current += bottle;
            }

            if(v.equals(imgGlass))
            {
                current += glass;
            }

            currentQuantity.setText(Integer.toString(current) + " " + quantityTypeML);
            progressBar.setProgress(current, true);

            if(current > target && !doOnce)
            {
                toast.show();
                doOnce = true;
            }
        }
    }
}