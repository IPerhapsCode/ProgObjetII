package com.example.a97cartestpfinal.alertDialogsActivity;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a97cartestpfinal.HighScoresActivity;
import com.example.a97cartestpfinal.MainActivity;
import com.example.a97cartestpfinal.MainMenu;
import com.example.a97cartestpfinal.R;
import com.example.a97cartestpfinal.db.Database;
import com.example.a97cartestpfinal.exceptions.ExceptionDB;

public class GameOver extends Dialog {

    private Ecouteur ec;

    private Button buttonHighscores, buttonMenu;
    private TextView textGameOver, textLose;
    private boolean winLose, exited = false;
    private Context context;
    private Database instance;

    public GameOver(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        this.ec = new Ecouteur();

        this.buttonHighscores = findViewById(R.id.button_highscores);
        this.buttonMenu = findViewById(R.id.button_return_menu);
        this.textGameOver = findViewById(R.id.text_game_over);
        this.textLose = findViewById(R.id.text_lose);

        this.buttonHighscores.setOnClickListener(this.ec);
        this.buttonMenu.setOnClickListener(this.ec);

        if(winLose)
        {
            this.textGameOver.setText("Félicitation");
            this.textLose.setText("Vous avez gagné!");
        }

        this.instance = Database.getInstance(this.context);
        this.instance.ouvrirConnexion();

        if(MainActivity.savedGame)
        {
            try
            {
                this.instance.deleteSavedGame();
            }
            catch(ExceptionDB e)
            {
                System.out.println(e.getMessage());
            }
            finally
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!this.exited)
        {
            this.dismiss();
            this.context.startActivity(new Intent(this.context, HighScoresActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
        }
    }

    public void setWinLose(boolean winLose)
    {
        this.winLose = winLose;
    }

    private class Ecouteur implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if(v.equals(buttonHighscores))
            {
                exited = true;
                dismiss();
                context.startActivity(new Intent(context, HighScoresActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
            else if(v.equals(buttonMenu))
            {
                exited = true;
                dismiss();
                context.startActivity(new Intent(context, MainMenu.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        }
    }
}