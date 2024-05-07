package com.example.a97cartestpfinal.alertDialogsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.example.a97cartestpfinal.MainActivity;
import com.example.a97cartestpfinal.R;
import com.google.android.material.chip.Chip;

public class Parametres extends Dialog {

    Ecouteur ec;
    Switch helper;
    Context context;
    boolean helperOn;

    public Parametres(@NonNull Context context, boolean helperOn) {
        super(context);
        this.context = context;
        this.helperOn = helperOn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        this.ec = new Ecouteur();

        this.helper = findViewById(R.id.switch_helper);
        this.helper.setOnClickListener(ec);

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
                }
                else if(context instanceof  MainActivity)
                {
                    ((MainActivity) context).stopHelperAi();
                }
            }
        }
    }
}