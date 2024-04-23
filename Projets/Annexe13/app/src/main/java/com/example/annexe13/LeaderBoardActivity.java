package com.example.annexe13;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class LeaderBoardActivity extends AppCompatActivity {

    Ecouteur ec;

    Button buttonSave;
    EditText inputNom, inputMicrobrasserie;
    RatingBar ratingBar;

    DatabaseHelper instance;

    AlertDialog.Builder builder;
    AlertDialog message;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        ec = new Ecouteur();
        instance = DatabaseHelper.getInstance(this);
        builder = new AlertDialog.Builder(this);
        toast = Toast.makeText(this, "Sélection enregistrer!", Toast.LENGTH_SHORT);

        buttonSave = findViewById(R.id.saveButton);
        inputNom = findViewById(R.id.inputNomBiere);
        inputMicrobrasserie = findViewById(R.id.inputMicrobrasserie);
        ratingBar = findViewById(R.id.ratingBar);

        buttonSave.setOnClickListener(ec);

        builder.setTitle("Error: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        instance.ouvrirConnection();
    }

    @Override
    protected void onStop() {
        super.onStop();
        instance.fermerConnection();
    }

    private class Ecouteur implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if(v.equals(buttonSave))
            {
                try
                {
                    if(inputNom.getText().toString().trim().length() == 0
                            || inputMicrobrasserie.getText().toString().trim().length() == 0)
                    {
                        throw new Exception("Aucun champ ne peut être vide!");
                    }
                    else
                    {
                        instance.enregistrerBiere(new Evaluation(inputNom.getText().toString().trim(),
                                inputMicrobrasserie.getText().toString().trim(), ratingBar.getRating()));

                        ratingBar.setRating(0);
                        inputNom.setText("");
                        inputMicrobrasserie.setText("");

                        toast.show();
                    }
                }
                catch (Exception e)
                {
                    builder.setMessage(e.getMessage());
                    message = builder.create();
                    message.show();

                    inputNom.setText("");
                    inputMicrobrasserie.setText("");
                }
            }
        }
    }
}