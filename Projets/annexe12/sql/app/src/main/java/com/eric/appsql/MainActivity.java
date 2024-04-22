package com.eric.appsql;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Random;
import java.util.Vector;


public class MainActivity extends AppCompatActivity {


    Ecouteur ec;
    GestionBD instance;
    ListView options;
    TextView question, reponse;
    Vector<String> inventeur, invention;
    String baseQuestion = "Quelle invention est créditée à ";
    int currentQuestion;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        instance = GestionBD.getInstance(getApplicationContext());
        options = findViewById(R.id.options);
        question = findViewById(R.id.questionText);
        reponse = findViewById(R.id.reponseText);

        ec = new Ecouteur();
        handler = new Handler();
        inventeur = new Vector<>(1, 1);
        invention = new Vector<>(1, 1);

        instance.ouvrirConnexion();
        Cursor cursor = instance.getDatabase().rawQuery("SELECT nom FROM inventeur", null);
        while(cursor.moveToNext())
        {
            inventeur.add(cursor.getString(0));
        }
        invention = instance.retournerInventions();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, invention);
        options.setAdapter(arrayAdapter);
        options.setOnItemClickListener(ec);

        currentQuestion = (int)(Math.ceil(inventeur.size() * Math.random())) - 1;
        System.out.println(currentQuestion);
        question.setText(baseQuestion + inventeur.get(currentQuestion) + "?");
        instance.femerConnexion();
//        instance.ouvrirConnexion();
//        Vector<String> v = instance.retournerInventions();
//        for(String s: v)
//        {
//            System.out.println(s);
//        }
//        System.out.println(instance.aBonneReponse("Laszlo Biro", "Stylo à bille"));
//        System.out.println(instance.aBonneReponse("Grace Hopper", "Essuie-glace"));
//        instance.femerConnexion();
    }

    @Override
    protected void onStop() {
        super.onStop();
        instance.femerConnexion();
    }

    private class Ecouteur implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            instance.ouvrirConnexion();
            try {
                if(instance.aBonneReponse(inventeur.get(currentQuestion), invention.get(position)))
                {
                    view.setBackgroundColor(Color.GREEN);
                    reponse.setText(R.string.bonne_reponse);
                    options.setOnItemClickListener(null);
                    inventeur.remove(currentQuestion);

                    if(inventeur.size() > 0)
                    {
                        handler.postDelayed(() -> {
                            currentQuestion = (int)(Math.ceil(inventeur.size() * Math.random())) - 1;
                            question.setText(baseQuestion + inventeur.get(currentQuestion) + "?");
                            reponse.setText("");
                            for(int i = 0; i < options.getChildCount(); ++i)
                            {
                                options.getChildAt(i).setBackgroundColor(Color.WHITE);
                            }
                            options.setOnItemClickListener(ec);
                        }, 3000);
                    }
                    else
                    {
                        reponse.setText(R.string.victoire);
                    }
                }
                else
                {
                    view.setBackgroundColor(Color.RED);
                    reponse.setText(R.string.mauvaise_reponse);
                }
            }
            catch(Exception e)
            {
                System.out.println("Couldn't open an instance of db!");
            }
            finally{
                instance.femerConnexion();
            }
        }
    }


}