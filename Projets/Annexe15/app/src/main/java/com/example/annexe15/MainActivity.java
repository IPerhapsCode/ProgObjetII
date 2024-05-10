package com.example.annexe15;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Ecouteur ec;
    EcouteurSpinner ecs;
    Database instance;
    TextView nbEquipe, nbSiege, reponse;
    Button search;
    Spinner spinner;

    String selectionSpinner = "";

    Vector<Equipe> equipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.ec = new Ecouteur();
        this.ecs = new EcouteurSpinner();

        this.instance = Database.getInstance(this);
        this.nbEquipe = findViewById(R.id.nb_equipe_ouest);
        this.nbSiege = findViewById(R.id.moyenne_nb_siege);
        this.reponse = findViewById(R.id.reponse_query);
        this.search = findViewById(R.id.button);
        this.spinner = findViewById(R.id.spinner);

        this.search.setOnClickListener(ec);
        this.spinner.setOnItemSelectedListener(ecs);

        //Opération à faire pendant que la base de données est ouverte et avant que l'activité ne démare
        this.instance.ouvrirConnexion();

        //Initialiser le spinner
        ArrayAdapter aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, this.instance.getArenas());
        this.spinner.setAdapter(aa);

        //Nb équipe dans la division ouest
        this.nbEquipe.setText(String.valueOf(this.instance.nbEquipeDivOuest()));

        //Moyenne nb siege dans une arena
        String siege = " siège";
        float nbSiege = this.instance.moyenneCapaciteArena();
        if(nbSiege >= 2)
        {
            siege += 's';
        }
        this.nbSiege.setText(String.valueOf(nbSiege) + siege);

        this.equipes = this.instance.getEquipes();

        for(Equipe i : this.equipes)
        {
            System.out.println(i.getNom());
        }

        this.instance.fermerConnexion();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.instance.fermerConnexion();
    }

    private class Ecouteur implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if(v.equals(search))
            {
                instance.ouvrirConnexion();
                reponse.setText(instance.getEquipe(selectionSpinner));
                instance.fermerConnexion();
            }
        }
    }

    private class EcouteurSpinner implements AdapterView.OnItemSelectedListener
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectionSpinner = ((TextView) view).getText().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}