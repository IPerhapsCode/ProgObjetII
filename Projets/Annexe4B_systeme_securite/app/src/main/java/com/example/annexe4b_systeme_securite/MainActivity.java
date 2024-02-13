package com.example.annexe4b_systeme_securite;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    Ecouteur ec;
    Vector<Button> buttons = new Vector(10,1);
    EditText passwordField;
    LinearLayout parentBouton, parent;
    String password = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ec = new Ecouteur();

        parent = findViewById(R.id.parent);
        parentBouton = findViewById(R.id.buttonParent);
        for(int i = 0; i < parentBouton.getChildCount(); ++i)
        {
            LinearLayout child = (LinearLayout) parentBouton.getChildAt(i);
            for(int j = 0; j < child.getChildCount(); ++j)
            {
                buttons.add((Button)child.getChildAt(j));
                buttons.lastElement().setOnClickListener(ec);
            }
        }
        passwordField = findViewById(R.id.passwordField);
        passwordField.setOnClickListener(ec);
    }

    private class Ecouteur implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if(!v.equals(passwordField))
            {
                Button temp = (Button)v;
                passwordField.setText(passwordField.getText().toString() + temp.getText());
            }

            if(passwordField.getText().toString().length() == 4
                    && password.equals(passwordField.getText().toString()))
            {
                parent.setBackgroundColor(Color.GREEN);
                passwordField.setText("");
            }
            else if(passwordField.getText().toString().length() == 4)
            {
                parent.setBackgroundColor(Color.RED);
                passwordField.setText("");
            }
        }
    }
}