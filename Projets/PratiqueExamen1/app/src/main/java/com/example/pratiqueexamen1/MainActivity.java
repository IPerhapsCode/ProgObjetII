package com.example.pratiqueexamen1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Ecouteur ec;
    Spinner debut;
    Spinner fin;
    Button enregistrer;
    ProgressBar progressBar;
    EditText typeEntrainement;
    Saison saison;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ec = new Ecouteur();
        saison = new Saison();

        debut = findViewById(R.id.spinnerDebut);
        fin = findViewById(R.id.spinnerFin);
        enregistrer = findViewById(R.id.register);
        typeEntrainement = findViewById(R.id.typeEntrainement);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(Saison.OBJECTIF);

        Vector<Integer> time = new Vector<>(1, 1);
        for(int i = 4; i <= 22; ++i)
        {
            time.add(i);
        }

        ArrayAdapter spinners = new ArrayAdapter(this, android.R.layout.simple_list_item_1, time);
        debut.setAdapter(spinners);
        fin.setAdapter(spinners);

        enregistrer.setOnClickListener(ec);
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            boolean fail = false;
            if(typeEntrainement.getText().toString().length() == 0)
            {
                fail = true;
                typeEntrainement.setHint("Ce champ ne peut être vide!");
            }

            if(((int)debut.getSelectedItem()) > ((int)fin.getSelectedItem()))
            {
                fail = true;
                typeEntrainement.setHint("Le temps d'entrainement ne peux être négatif!");
            }

            if(!fail)
            {
                saison.addEntrainement(new Entrainement(typeEntrainement.getText().toString(), (int)debut.getSelectedItem(), (int)fin.getSelectedItem()));
                typeEntrainement.setText("");
                typeEntrainement.setHint("Type d'entrainement");
            }

            if(progressBar.getProgress() != saison.nbHeuresEntrainees())
            {
                progressBar.setProgress(saison.nbHeuresEntrainees());
            }
        }
    }
}