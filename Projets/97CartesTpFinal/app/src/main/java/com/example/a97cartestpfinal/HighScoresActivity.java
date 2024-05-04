package com.example.a97cartestpfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a97cartestpfinal.db.Database;

public class HighScoresActivity extends AppCompatActivity {

    Ecouteur ec;
    Database instance;
    LinearLayout highscoreZone;
    Button menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        ec = new Ecouteur();

        highscoreZone = findViewById(R.id.highscore_zone);
        menu = findViewById(R.id.button_menu);

        menu.setOnClickListener(ec);

        //Ouverture de l'instance de notre base de donnée
        instance = Database.getInstance(this);
        instance.ouvrirConnexion();

        //Création du leaderboard
        this.createHighScoreTable(instance.getHighScores());
    }

    @Override
    protected void onStop() {
        super.onStop();
        instance.fermerConnexion();
    }

    //Affiche les highscores du plus grand au plus petit
    private void createHighScoreTable(Cursor c)
    {
        //Création des params nécessaire à l'affichage
        int valeurPosition = 1;
        float densite = getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f);
        params.gravity = Gravity.CENTER;
        params.setMargins((int)(5 * densite), 0, (int)(5 * densite), (int)(10 * densite));
        LinearLayout.LayoutParams paramsText = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.29f);
        LinearLayout.LayoutParams paramsPosition = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.13f);

        while(c.moveToNext())
        {
            //Ajoute le layout qui va contenir les valeurs
            LinearLayout scoreZone = new LinearLayout(this);
            scoreZone.setBackground(getResources().getDrawable(R.drawable.background_leaderboard_values));
            scoreZone.setOrientation(LinearLayout.HORIZONTAL);
            scoreZone.setLayoutParams(params);
            highscoreZone.addView(scoreZone);

            //Ajoute la position du score
            TextView position = new TextView(this);
            position.setLayoutParams(paramsPosition);
            position.setGravity(Gravity.CENTER);
            position.setText(String.valueOf(valeurPosition) + ".");
            scoreZone.addView(position);


            //Ajoute les valeurs au layout
            for(int i = 0; i < 3; ++i)
            {
                TextView t = new TextView(this);
                t.setLayoutParams(paramsText);
                t.setGravity(Gravity.CENTER);
                t.setPadding(0, (int)(10 * densite), 0, (int)(10 * densite));
                switch(i)
                {
                    case 0:
                    case 1:{
                        t.setText(String.valueOf(c.getInt(i)).trim());
                        break;
                    }
                    case 2:{
                        t.setText(c.getString(i).trim());
                        break;
                    }
                }
                scoreZone.addView(t);
            }
            ++valeurPosition;
        }
    }

    private class Ecouteur implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(HighScoresActivity.this, MainMenu.class));
        }
    }
}