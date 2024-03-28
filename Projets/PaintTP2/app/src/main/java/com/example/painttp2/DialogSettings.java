package com.example.painttp2;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Handler;

import com.example.painttp2.shapes.Efface;

//Cette classe va devenir un scrollview vertical contenant toutes les modifications pour toutes classes
public class DialogSettings extends Dialog {

    Ecouteur ec;
    EcouteurSpinner ecSpinner;

    ScrollView parent;
    TextView largeurTrait;
    SeekBar seekBarLargeurTrait;
    Button boutonConfirmLargeurTrait, boutonByebye;
    Spinner spinnerDessinLibre, spinnerTriangle, spinnerFill;

    int progress = MainActivity.getSizeTrace();
    String largeurTraitText = "";
    final Handler handler = new Handler();

    public DialogSettings(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_largeur_trait);

        ec = new Ecouteur();
        ecSpinner = new EcouteurSpinner();

        parent = findViewById(R.id.scrollview_dialog);
        largeurTrait = findViewById(R.id.largeur_trait_text);
        seekBarLargeurTrait = findViewById(R.id.seekBar);
        boutonConfirmLargeurTrait = findViewById(R.id.confirm);
        boutonByebye = findViewById(R.id.button_byebye);
        spinnerDessinLibre = findViewById(R.id.spinner_dessin);
        spinnerTriangle = findViewById(R.id.spinner_triangle);
        spinnerFill = findViewById(R.id.spinner_fill);

        progress = MainActivity.getSizeTrace();
        seekBarLargeurTrait.setMax(50);
        seekBarLargeurTrait.setProgress(progress);
        seekBarLargeurTrait.setOnTouchListener(ec);

        largeurTraitText = largeurTrait.getText().toString();
        largeurTrait.setText(largeurTraitText + progress);

        boutonConfirmLargeurTrait.setOnTouchListener(ec);
        boutonByebye.setOnTouchListener(ec);

        String[] listeDessin = new String[]{"Forme libre", "Forme fermée"},
                listeTriangle = new String[]{"Équilatérale", "Rectangle", "Libre horizontale", "Libre verticale"},
                listeFill = new String[]{"Lent", "Rapide"};

        spinnerDessinLibre.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listeDessin));
        spinnerTriangle.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listeTriangle));
        spinnerFill.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, listeFill));

        spinnerDessinLibre.setOnItemSelectedListener(ecSpinner);
        spinnerTriangle.setOnItemSelectedListener(ecSpinner);
        spinnerFill.setOnItemSelectedListener(ecSpinner);
    }

    private class Ecouteur implements View.OnTouchListener
    {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //Change la taille du trait des formes
            if(v.equals(seekBarLargeurTrait) && (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE))
            {
                //Basically for god only knows what reason
                //Si on clique sur le seek bar, la valeur n'a pas le temps d'être mis à jour avant que ce code ne soit lancé
                //Il est donc nécessaire de mettre un délai ici pour s'assurer que la valeur soit enregistré si l'utilisateur clique sur la seek bar
                handler.postDelayed(() -> {
                    progress = seekBarLargeurTrait.getProgress();
                    MainActivity.setSizeTrace(progress);
                    Efface.setSizeTraceEfface(Math.min(2 * progress, 50));
                    largeurTrait.setText(largeurTraitText + progress);
                    System.out.println(progress);
                }, 100);
            } //Ferme la fenêtre de dialogue lorsque le joueur confirme la taille de son trait
            else if(v.equals(boutonConfirmLargeurTrait) && event.getAction() == MotionEvent.ACTION_UP)
            {
                closeAlertDialog();
            } //Vide la zone de dessin
            else if(v.equals(boutonByebye) && event.getAction() == MotionEvent.ACTION_UP)
            {
                MainActivity.clearScreen();
            }
            return false;
        }
    }

    private class EcouteurSpinner implements AdapterView.OnItemSelectedListener
    {
        //Change des options pour certaines formes
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(parent.equals(spinnerDessinLibre))
            {
                MainActivity.setStyleDessinLibre(position);
            }
            else if(parent.equals(spinnerTriangle))
            {
                MainActivity.setStyleTriangle(position);
            }
            else if(parent.equals(spinnerFill))
            {
                MainActivity.setStyleFill(position);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    //Ferme la fenêtre de dialogue
    private void closeAlertDialog()
    {
        this.dismiss();
    }
}