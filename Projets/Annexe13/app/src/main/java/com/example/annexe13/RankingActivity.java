package com.example.annexe13;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RankingActivity extends AppCompatActivity {

    DatabaseHelper instance;
    AlertDialog.Builder builder;
    AlertDialog message;

    ListView leaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        instance = DatabaseHelper.getInstance(this);
        builder = new AlertDialog.Builder(this);

        leaderboard = findViewById(R.id.leaderboard);

        builder.setTitle("Error");
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance.ouvrirConnection();

        try
        {
            leaderboard.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, instance.voirTopTrois()));
        }
        catch (Exception e)
        {
            builder.setMessage(e.getMessage());
            message = builder.create();
            message.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        instance.fermerConnection();
    }
}