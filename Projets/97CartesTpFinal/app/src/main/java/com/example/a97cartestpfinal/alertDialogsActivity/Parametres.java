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
import com.google.android.material.chip.Chip;

import java.util.Vector;

public class Parametres extends Dialog {

    private Ecouteur ec;
    private Switch helper;
    private Vector<Switch> couleurs;
    private LinearLayout parentCouleurs;
    private Context context;
    private boolean helperOn;
    private int couleurChoisi;

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

        this.couleurs = new Vector<>(1, 1);

        this.parentCouleurs = findViewById(R.id.zone_couleur);
        this.helper = findViewById(R.id.switch_helper);
        this.helper.setOnClickListener(this.ec);

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

        if(this.helperOn)
        {
            this.helper.setChecked(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.dismiss();
    }

    private class Ecouteur implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            if(v.equals(helper))
            {
                helperOn = ((Switch) v).isChecked();

                if(helperOn && context instanceof MainActivity)
                {
                    ((MainActivity) context).helperAi();
                    ((MainActivity) context).setHelper(true);
                }
                else if(context instanceof  MainActivity)
                {
                    ((MainActivity) context).stopHelperAi();
                    ((MainActivity) context).setHelper(false);
                }
            }
            else if(couleurs.contains(v))
            {
                for(Switch i : couleurs)
                {
                    if(v.equals(i))
                    {
                        couleurChoisi = Integer.parseInt(v.getTag().toString());
                        ((MainActivity) context).changeCardColor(couleurChoisi);
                    }
                    else
                    {
                        i.setChecked(false);
                    }
                }
            }
        }
    }
}