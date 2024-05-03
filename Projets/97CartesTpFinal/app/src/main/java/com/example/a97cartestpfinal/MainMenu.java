package com.example.a97cartestpfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class MainMenu extends AppCompatActivity {

    Ecouteur ec;
    Button buttonNewGame, buttonContinue;
    TextView highscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ec = new Ecouteur();

        buttonNewGame = findViewById(R.id.button_new_game);
        buttonContinue = findViewById(R.id.button_continue);
        highscore = findViewById(R.id.textview_highscore);

        buttonNewGame.setOnClickListener(ec);
        buttonContinue.setOnClickListener(ec);

        //If there is no game to continue gray out the button
        buttonContinue.setBackgroundColor(getResources().getColor(R.color.actual_grey));
        buttonContinue.setTextColor(getResources().getColor(R.color.actual_grey_dark));
        ((MaterialButton)buttonContinue).setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.actual_grey_light)));
    }

    private class Ecouteur implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {

        }
    }
}