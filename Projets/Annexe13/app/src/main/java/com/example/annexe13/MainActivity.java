package com.example.annexe13;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Ecouteur ec;

    private Button buttonRanking, buttonLeaderboard;
    private Intent ranking, leaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ec = new Ecouteur();

        buttonRanking = findViewById(R.id.goToRankingButton);
        buttonLeaderboard = findViewById(R.id.goToLeaderboardButton);

        buttonRanking.setOnClickListener(ec);
        buttonLeaderboard.setOnClickListener(ec);

        ranking = new Intent(this, LeaderBoardActivity.class);
        leaderboard = new Intent(this, RankingActivity.class);
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(v.equals(buttonRanking))
            {
                startActivity(ranking);
            }
            else if(v.equals(buttonLeaderboard))
            {
                startActivity(leaderboard);
            }
        }
    }
}