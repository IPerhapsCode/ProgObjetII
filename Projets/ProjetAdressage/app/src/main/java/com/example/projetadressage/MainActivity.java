package com.example.projetadressage;

import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;


import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Collection;
import java.util.Vector;

import bla.HashtableAssociation;


public class MainActivity extends AppCompatActivity {

    EditText champPrenom, champNom, champAdresse, champZip;
    Spinner spinnerCapitale, spinnerEtat;

    Button bouton;
    
    HashtableAssociation hashtableAssociation;

    Ecouteur ec;

    Vector<Inscrit> inscriptions;

    AlertDialog.Builder messageBuilder;
    AlertDialog message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ec = new Ecouteur();

        champPrenom = findViewById(R.id.champPrenom);
        champNom= findViewById(R.id.champNom);
        champAdresse = findViewById(R.id.champAdresse);
        champZip = findViewById(R.id.champZip);

        spinnerCapitale = findViewById(R.id.spinnerCapitale);
        spinnerEtat = findViewById(R.id.spinnerEtat);

        bouton = findViewById(R.id.boutonInscrire);
        bouton.setOnClickListener(ec);

        inscriptions = new Vector<>(1, 1);

        messageBuilder = new AlertDialog.Builder(this).setTitle("Error!");

        // remplir les spinner à l'aide de la Hashtable
        hashtableAssociation = new HashtableAssociation();
        Vector<String> possibleCapitales = new Vector<>(hashtableAssociation.keySet());
        Vector<String> possibleEtats = new Vector<>(hashtableAssociation.values());
//        for(String i : hashtableAssociation.keySet())
//        {
//            possibleCapitales.add(i);
//            if(!possibleEtats.contains(hashtableAssociation.get(i)))
//            {
//                possibleEtats.add(hashtableAssociation.get(i));
//            }
//        }

        ArrayAdapter adapterCapitale = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, possibleCapitales);
        ArrayAdapter adapterEtat = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, possibleEtats);

        spinnerCapitale.setAdapter(adapterCapitale);
        spinnerEtat.setAdapter(adapterEtat);
    }
    private class Ecouteur implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            try {
                inscriptions.add(new Inscrit(champNom.getText().toString(), champPrenom.getText().toString(),
                        champAdresse.getText().toString(), spinnerCapitale.getSelectedItem().toString(),
                        spinnerEtat.getSelectedItem().toString(), champZip.getText().toString()));
            }
            catch (AdresseException ae)
            {
                messageBuilder.setMessage(ae.getMessage());
                message = messageBuilder.create();
                message.show();
                ae.printStackTrace();
            }
        }
    }
}