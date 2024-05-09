package com.example.a97cartestpfinal.alertDialogsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.example.a97cartestpfinal.MainActivity;
import com.example.a97cartestpfinal.R;
import com.example.a97cartestpfinal.db.Database;
import com.example.a97cartestpfinal.exceptions.ExceptionDB;
import com.google.android.material.chip.Chip;

import java.util.Vector;

public class Parametres extends Dialog {

    private Ecouteur ec;
    private Switch helper;
    private Vector<Switch> couleurs;
    private LinearLayout parentCouleurs;
    private Context context;
    Database instance;
    private boolean helperOn, dbState = false;
    private int couleurChoisi;

    //Permet d'obtenir si le helper est on, la couleur de préférence ainsi que le contexte
    public Parametres(@NonNull Context context, boolean helperOn, int couleurChoisi) {
        super(context);
        this.context = context;
        this.helperOn = helperOn;
        this.couleurChoisi = couleurChoisi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        this.ec = new Ecouteur();

        this.instance = Database.getInstance(this.context);

        this.couleurs = new Vector<>(1, 1);

        this.parentCouleurs = findViewById(R.id.zone_couleur);
        this.helper = findViewById(R.id.switch_helper);
        this.helper.setOnClickListener(this.ec);

        //Obtient toutes les switchs de changement de couleur
        for(int i = 0; i < this.parentCouleurs.getChildCount(); ++i)
        {
            this.couleurs.add((Switch)this.parentCouleurs.getChildAt(i));
            this.couleurs.lastElement().setOnClickListener(ec);
            if(Integer.parseInt(this.couleurs.lastElement().getTag().toString()) == this.couleurChoisi)
            {
                this.couleurs.lastElement().setChecked(true);
            }
            else
            {
                this.couleurs.lastElement().setChecked(false);
            }
        }

        //Change visuellement si le helper est ouvert ou non
        if(this.helperOn)
        {
            this.helper.setChecked(true);
        }
    }

    //Assure que la connexion à la base de donnée est fermée lors de la fermeture de l'alert dialog
    @Override
    protected void onStop() {
        super.onStop();

        if(this.dbState)
        {
            try
            {
                this.instance.fermerConnexion();
            }
            catch(ExceptionDB e)
            {
                System.out.println(e.getMessage());
            }
        }

        this.dismiss();
    }

    //Permet de sauvegarder les préférences si l'utilisateur change quelque chose dans le menu
    private void savePreferences()
    {
        this.dbState = this.instance.ouvrirConnexion();
        try
        {
            this.instance.savePreferences(this.helperOn, this.couleurChoisi);
        }
        catch (ExceptionDB e)
        {
            System.out.println(e.getMessage());
        }
        finally
        {
            try
            {
                this.dbState = this.instance.fermerConnexion();
            }
            catch(ExceptionDB e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    private class Ecouteur implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            //Change si le helper est activé ou non
            if(v.equals(helper))
            {
                helperOn = ((Switch) v).isChecked();

                if(helperOn && context instanceof MainActivity)
                {
                    ((MainActivity) context).helperAi();
                    ((MainActivity) context).setHelper(helperOn);
                }
                else if(context instanceof  MainActivity)
                {
                    ((MainActivity) context).stopHelperAi();
                    ((MainActivity) context).setHelper(helperOn);
                }
            }
            //Permet de choisir la couleur des cartes et assure qu'au moins une des switchs de couleur est sélectionnées
            else if(couleurs.contains(v))
            {
                boolean oneChecked = false;
                for(Switch i : couleurs)
                {
                    if(v.equals(i))
                    {
                        couleurChoisi = Integer.parseInt(v.getTag().toString());
                        ((MainActivity) context).changeCardColor(couleurChoisi);
                        oneChecked = ((Switch) v).isChecked();
                    }
                    else
                    {
                        i.setChecked(false);
                    }
                }

                if(!oneChecked)
                {
                    ((Switch) v).setChecked(true);
                }
            }

            savePreferences();
        }
    }
}