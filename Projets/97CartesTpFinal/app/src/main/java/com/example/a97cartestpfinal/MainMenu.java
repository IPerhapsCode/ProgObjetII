package com.example.a97cartestpfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a97cartestpfinal.db.Database;
import com.google.android.material.button.MaterialButton;

public class MainMenu extends AppCompatActivity {

    private Ecouteur ec;
    private Button buttonNewGame, buttonContinue;
    private TextView highscore;
    private Database instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.ec = new Ecouteur();

        this.instance = Database.getInstance(this);
        this.instance.ouvrirConnexion();

        this.buttonNewGame = findViewById(R.id.button_new_game);
        this.buttonContinue = findViewById(R.id.button_continue);
        this.highscore = findViewById(R.id.textview_highscore);

        this.buttonNewGame.setOnClickListener(this.ec);
        this.buttonContinue.setOnClickListener(this.ec);

        //If there is no game to continue gray out the button
        this.buttonContinue.setBackgroundColor(getResources().getColor(R.color.actual_grey));
        this.buttonContinue.setTextColor(getResources().getColor(R.color.actual_grey_dark));
        ((MaterialButton)this.buttonContinue).setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.actual_grey_light)));

        //Affiche le highest score
        this.highscore.setText("HighScore: " + this.instance.getHighestScore() + " points");
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.instance.fermerConnexion();
        this.finish();
    }

    private class Ecouteur implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
                if(v.equals(buttonNewGame))
                {
                    startActivity(new Intent(MainMenu.this, MainActivity.class));
                }
                else if(v.equals(buttonContinue))
                {

                }
        }
    }
}