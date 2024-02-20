package com.example.premiereapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Hashtable;

public class Annexe2 extends AppCompatActivity {

    Ecouteur ec;
    Button boutonValider;

    Spinner spinnerNomCompte;
    TextView champSolde;

    Button boutonEnvoyer;

    EditText champCourriel;

    EditText champMontant;
    String nomCompte;
    String[] comptePossible = {"Cheque", "Epargne", "EpargnePlus"};
    double[] valeurComptesPossible = {34.43, 491.01, 98.75};
    String compteInvalide = "Nom de compte invalide!";
    //Compte[] comptes = {new Compte(valeurComptesPossible[0], comptePossible[0]), new Compte(valeurComptesPossible[1], comptePossible[1]), new Compte(valeurComptesPossible[2], comptePossible[2])};
    Hashtable<String, Compte> comptes = new Hashtable();
    boolean compteValide = false;

    String courrielVide = "Veuillez choisir un destinataire!";
    String montantVide = "Veuillez choisir un montant!";
    String patternMontant = "\\d+([.]?)(\\d*)";
    String patternCourriel = ".+@[a-z]+[.][a-z]+([.][a-z])?";

    boolean courrielValide = false;
    boolean montantValide = false;

    DecimalFormat input;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annexe2);

        //Initialiser variables
        boutonValider = findViewById(R.id.ValidateButton);
        spinnerNomCompte = findViewById(R.id.FromSpinner);
        champSolde = findViewById(R.id.SoldField);
        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, comptePossible);

        boutonEnvoyer = findViewById(R.id.SendButton);
        champCourriel = findViewById(R.id.ToField);
        champMontant = findViewById(R.id.TransferAmountField);

        input = new DecimalFormat("0.00$");

        for(int i = 0; i < comptePossible.length; ++i)
        {
            comptes.put(comptePossible[i], new Compte(valeurComptesPossible[i], comptePossible[i]));
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, comptes.keySet().toArray(new String[0]));
        spinnerNomCompte.setAdapter(adapter);

        builder = new AlertDialog.Builder(this);

        //1er étape : création de l'écouteur
        ec = new Ecouteur();

        //2ième étape : inscrire la source à l'écouteur
        boutonValider.setOnClickListener(ec);
        boutonEnvoyer.setOnClickListener(ec);
        spinnerNomCompte.setOnItemSelectedListener(ec);
    }

    //3ième étape : code une classe pour notre écouteur/listener
    private class Ecouteur implements View.OnClickListener, AdapterView.OnItemSelectedListener
    {
        @Override
        public void onClick(View v)
        {
            if(v.equals(boutonValider))
            {
                champSolde.setText(input.format(comptes.get(nomCompte).getSolde()));
                compteValide = true;
            }

            if(v.equals(boutonEnvoyer))
            {
                if(champCourriel.getText().toString().length() == 0)
                {
                    champCourriel.setHint(courrielVide);
                    courrielValide = false;
                }
                else if(champCourriel.getText().toString().matches(patternCourriel))
                {
                    courrielValide = true;
                }
                else
                {
                    champCourriel.setText("");
                    builder.setMessage("Courriel invalide!").setTitle("Erreur");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    courrielValide = false;
                }

                if(champMontant.getText().toString().length() == 0)
                {
                    champMontant.setHint(montantVide);
                    montantValide = false;
                }
                else if(champMontant.getText().toString().trim().matches(patternMontant))
                {
                    montantValide = true;
                }
                else
                {
                    champMontant.setText("");
                    builder.setMessage("Montant invalide!").setTitle("Erreur");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    montantValide = false;
                }

                if(montantValide && compteValide && courrielValide)
                {
                    double montant = new Double(champMontant.getText().toString()).doubleValue();
                    Compte account = comptes.get(nomCompte);

                    if(account.getSolde() >= montant)
                    {
                        account.transfert(montant);
                        champSolde.setText(String.valueOf(input.format(account.getSolde())));
                    }
                    else
                    {
                        champMontant.setText("");
                        builder.setMessage("Montant plus grand que le solde!").setTitle("Erreur");
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        montantValide = false;
                    }
                }
            }
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            nomCompte = comptePossible[position];

            //Autre méthode
            TextView temp = (TextView) view;
            nomCompte = temp.getText().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}