package com.example.painttp2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.painttp2.shapes.Efface;

//Cette classe va devenir un scrollview vertical contenant toutes les modifications pour toutes classes
public class Dialog_Largeur_Trait extends Dialog {

    Ecouteur ec;

    TextView largeurTrait;
    SeekBar seekBar;
    Button confirm;

    int progress = 0;
    String largeurTraitText = "";

    public Dialog_Largeur_Trait(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_largeur_trait);

        ec = new Ecouteur();

        largeurTrait = findViewById(R.id.largeur_trait_text);
        seekBar = findViewById(R.id.seekBar);
        confirm = findViewById(R.id.confirm);

        progress = MainActivity.getSizeTrace();
        seekBar.setMax(100);
        seekBar.setProgress(progress);
        seekBar.setOnTouchListener(ec);

        largeurTraitText = largeurTrait.getText().toString();
        largeurTrait.setText(largeurTraitText + progress);

        confirm.setOnTouchListener(ec);
    }

    private class Ecouteur implements View.OnTouchListener
    {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(v.equals(seekBar))
            {
                int progress = seekBar.getProgress();
                MainActivity.setSizeTrace(progress);
                Efface.setSizeTraceEfface(Math.min(4 * progress, 100));
                largeurTrait.setText(largeurTraitText + progress);
            }
            else if(v.equals(confirm) && event.getAction() == MotionEvent.ACTION_UP)
            {
                closeAlertDialog();
            }
            return false;
        }
    }

    private void closeAlertDialog()
    {
        this.cancel();
    }
}