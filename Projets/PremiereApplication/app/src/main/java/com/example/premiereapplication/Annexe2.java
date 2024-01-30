package com.example.premiereapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Annexe2 extends AppCompatActivity {

    Ecouteur ec;
    Button boutonValider;

    EditText champNomCompte;
    TextView champSolde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annexe2);

        //Initialiser variables
        boutonValider = findViewById(R.id.ValidateButton);
        champNomCompte = findViewById(R.id.FromField);
        champSolde = findViewById(R.id.SoldField);

        //1er étape : création de l'écouteur
        ec = new Ecouteur();

        //2ième étape : inscrire la source à l'écouteur
        boutonValider.setOnClickListener(ec);

    }

    //3ième étape : code une classe pour notre écouteur/listener
    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            champSolde.setText("allo");
        }
    }
}