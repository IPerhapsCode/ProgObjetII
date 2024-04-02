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

    private Ecouteur ec;
    private EcouteurSpinner ecSpinner;
    private EcouteurSeekBar ecSeekBar;

    private ScrollView parent;
    private TextView largeurTrait;
    private SeekBar seekBarLargeurTrait;
    private Button boutonConfirmLargeurTrait, boutonByebye;
    private Spinner spinnerDessinLibre, spinnerTriangle, spinnerFill;

    private int myProgress = MainActivity.getSizeTrace();
    private String largeurTraitText = "";
    private final Handler handler = new Handler();

    public DialogSettings(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_largeur_trait);

        ec = new Ecouteur();
        ecSpinner = new EcouteurSpinner();
        ecSeekBar = new EcouteurSeekBar();

        parent = findViewById(R.id.scrollview_dialog);
        largeurTrait = findViewById(R.id.largeur_trait_text);
        seekBarLargeurTrait = findViewById(R.id.seekBar);
        boutonConfirmLargeurTrait = findViewById(R.id.confirm);
        boutonByebye = findViewById(R.id.button_byebye);
        spinnerDessinLibre = findViewById(R.id.spinner_dessin);
        spinnerTriangle = findViewById(R.id.spinner_triangle);
        spinnerFill = findViewById(R.id.spinner_fill);

        myProgress = MainActivity.getSizeTrace();
        seekBarLargeurTrait.setMax(50);
        seekBarLargeurTrait.setProgress(myProgress);
        seekBarLargeurTrait.setOnSeekBarChangeListener(ecSeekBar);

        largeurTraitText = largeurTrait.getText().toString();
        largeurTrait.setText(largeurTraitText + myProgress);

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
            //Ferme la fenêtre de dialogue lorsque le joueur confirme la taille de son trait
            if(v.equals(boutonConfirmLargeurTrait) && event.getAction() == MotionEvent.ACTION_UP)
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

    private class EcouteurSeekBar implements SeekBar.OnSeekBarChangeListener
    {
        //Change la taille du trait des formes ainsi que l'efface indirectement
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            myProgress = progress;
            MainActivity.setSizeTrace(myProgress);
            Efface.setSizeTraceEfface(Math.min(2 * myProgress, 80));
            largeurTrait.setText(largeurTraitText + myProgress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    //Ferme la fenêtre de dialogue
    private void closeAlertDialog()
    {
        this.dismiss();
    }
}