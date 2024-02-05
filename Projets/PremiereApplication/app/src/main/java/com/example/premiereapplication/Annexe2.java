package com.example.premiereapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.*;

public class Annexe2 extends AppCompatActivity {

    Ecouteur ec;
    Button boutonValider;

    EditText champNomCompte;
    TextView champSolde;

    Button boutonEnvoyer;

    EditText champCourriel;

    EditText champMontant;

    String[] comptePossible = {"Cheque", "Epargne", "EpargnePlus"};
    String compteInvalide = "Nom de compte invalide!";
    double[] valeurComptesPossible = {34.43, 491.01, 98.75};
    boolean compteValide = false;

    String courrielVide = "Veuillez choisir un destinataire!";
    String montantVide = "Veuillez choisir un montant!";
    Pattern patternMontant = Pattern.compile("\\d+([.]?)(\\d*)");

    boolean courrielValide = false;
    boolean montantValide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annexe2);

        //Initialiser variables
        boutonValider = findViewById(R.id.ValidateButton);
        champNomCompte = findViewById(R.id.FromField);
        champSolde = findViewById(R.id.SoldField);

        boutonEnvoyer = findViewById(R.id.SendButton);
        champCourriel = findViewById(R.id.ToField);
        champMontant = findViewById(R.id.TransferAmountField);

        //1er étape : création de l'écouteur
        ec = new Ecouteur();

        //2ième étape : inscrire la source à l'écouteur
        boutonValider.setOnClickListener(ec);
        boutonEnvoyer.setOnClickListener(ec);
    }

    //3ième étape : code une classe pour notre écouteur/listener
    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(v.equals(boutonValider))
            {
                compteValide = false;

                for (String i:comptePossible)
                {
                    if(i.toUpperCase().equals(champNomCompte.getText().toString().toUpperCase()))
                    {
                        compteValide = true;
                        break;
                    }
                }

                if(!compteValide)
                {
                    champSolde.setText(compteInvalide);
                    champNomCompte.setText("");
                }
                else
                {
                    for (int i = 0; i < comptePossible.length; ++i)
                    {
                        if(comptePossible[i].toUpperCase().equals(champNomCompte.getText().toString().trim().toUpperCase()))
                        {
                            champSolde.setText(String.valueOf(valeurComptesPossible[i])+"$");
                        }
                    }
                }
            }

            if(v.equals(boutonEnvoyer))
            {
                if(champCourriel.getText().toString().length() == 0)
                {
                    champCourriel.setHint(courrielVide);
                    courrielValide = false;
                }
                else
                {
                    courrielValide = true;
                }

                Matcher m = patternMontant.matcher(champMontant.getText().toString().trim());
                if(champMontant.getText().toString().length() == 0)
                {
                    champMontant.setHint(montantVide);
                    montantValide = false;
                }
                else if(m.matches())
                {
                    montantValide = true;
                }
                else
                {
                    champMontant.setText("");
                    champMontant.setHint("Montant invalide!");
                    montantValide = false;
                }

                if(montantValide && compteValide)
                {
                    double montant = new Double(champMontant.getText().toString()).doubleValue();
                    int accountNb = 0;
                    for (int i = 0; i < comptePossible.length; ++i)
                    {
                        if(comptePossible[i].equals(champNomCompte.getText().toString()))
                        {
                            accountNb = i;
                            break;
                        }
                    }

                    if(valeurComptesPossible[accountNb] >= montant)
                    {
                        valeurComptesPossible[accountNb] -= montant;
                        champSolde.setText(String.valueOf(valeurComptesPossible[accountNb]));
                    }
                    else
                    {
                        champMontant.setText("");
                        champMontant.setHint("Montant plus grand que le solde!");
                        montantValide = false;
                    }
                }
            }
        }
    }
}