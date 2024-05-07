package com.example.a97cartestpfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a97cartestpfinal.db.Database;
import com.example.a97cartestpfinal.exceptions.ExceptionDB;
import com.google.android.material.button.MaterialButton;

public class MainMenu extends AppCompatActivity {

    private Ecouteur ec;
    private Button buttonNewGame, buttonContinue;
    private TextView highscore;
    private Database instance;
    private boolean dbState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.ec = new Ecouteur();

        this.instance = Database.getInstance(this);

        this.buttonNewGame = findViewById(R.id.button_new_game);
        this.buttonContinue = findViewById(R.id.button_continue);
        this.highscore = findViewById(R.id.textview_highscore);

        this.buttonNewGame.setOnClickListener(this.ec);
        this.buttonContinue.setOnClickListener(this.ec);

        //If there is no game to continue gray out the button
        this.dbState = this.instance.ouvrirConnexion();
        try
        {
            if(!this.instance.hasSavedGame())
            {
                this.buttonContinue.setBackgroundColor(getResources().getColor(R.color.actual_grey));
                this.buttonContinue.setTextColor(getResources().getColor(R.color.actual_grey_dark));
                ((MaterialButton)this.buttonContinue).setStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.actual_grey_light)));
            }
        }
        catch (ExceptionDB e)
        {
            System.out.println(e.getMessage());
        }

        //Affiche le highest score
        try
        {
            this.highscore.setText("HighScore: " + this.instance.getHighestScore() + " points");
        }
        catch (ExceptionDB e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            try
            {
                this.dbState = this.instance.fermerConnexion();
            }
            catch (ExceptionDB e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(this.dbState)
        {
            try
            {
                this.instance.fermerConnexion();
            }
            catch (ExceptionDB e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    private class Ecouteur implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
                if(v.equals(buttonNewGame))
                {
                    MainActivity.savedGame = false;
                    startActivity(new Intent(MainMenu.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                }
                else if(v.equals(buttonContinue))
                {
                    dbState = instance.ouvrirConnexion();
                    try
                    {
                        if(instance.hasSavedGame())
                        {
                            MainActivity.savedGame = true;
                            startActivity(new Intent(MainMenu.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                        }
                    }
                    catch (ExceptionDB e)
                    {
                        System.out.println(e.getMessage());
                    }

                    try
                    {
                        dbState = instance.fermerConnexion();
                    }
                    catch (ExceptionDB e)
                    {
                        System.out.println(e.getMessage());
                    }
                }
        }
    }
}