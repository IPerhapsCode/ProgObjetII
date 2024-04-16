package com.eric.appsql;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import java.util.Vector;


public class MainActivity extends AppCompatActivity {






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        GestionBD instance = GestionBD.getInstance(getApplicationContext());
        instance.ouvrirConnexion();
        Vector<String> v = instance.retournerInventions();
        for(String s: v)
        {
            System.out.println(s);
        }
        System.out.println(instance.aBonneReponse("Laszlo Biro", "Stylo à bille"));
        System.out.println(instance.aBonneReponse("Grace Hopper", "Essuie-glace"));
        instance.femerConnexion();
    }





}