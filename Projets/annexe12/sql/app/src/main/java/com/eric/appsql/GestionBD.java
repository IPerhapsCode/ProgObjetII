package com.eric.appsql;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;


public class GestionBD extends SQLiteOpenHelper {

    //instance unique de la classe Singleton GestionBD
    private static GestionBD instance;
    private SQLiteDatabase database;


    // méthode de base pour un Singleton
    public static GestionBD getInstance(Context contexte) {
        if (instance == null)
            instance = new GestionBD(contexte);
        return instance;
    }


    private GestionBD(Context context) {
        super(context, "db", null, 1);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table inventeur(_id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, " +
                "origine TEXT, invention TEXT, annee INTEGER)");
        ajouterInventeur(new Inventeur("Laszlo Biro", "Hongrie", "Stylo à bille", 1938), db);
        ajouterInventeur(new Inventeur("Benjamin Franklin", "Etats-Unis", "Paratonnerre", 1752), db);
        ajouterInventeur(new Inventeur("Mary Anderson", "Etats-Unis", "Essuie-glace", 1903), db);
        ajouterInventeur(new Inventeur("Grace Hopper", "Etats-Unis", "Compilateur", 1952), db);
        ajouterInventeur(new Inventeur("Benoit Rouquayrot", "France", "Scaphandre", 1864), db);
    }

    public void ajouterInventeur(Inventeur i, SQLiteDatabase db)
    {
        ContentValues cv = new ContentValues();
        cv.put("nom", i.getNom().toLowerCase());
        cv.put("origine", i.getOrigine().toLowerCase());
        cv.put("invention", i.getInvention().toLowerCase());
        cv.put("annee", i.getAnnee());
        db.insert("inventeur", null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void ouvrirConnexion()
    {
        database = this.getWritableDatabase();
    }

    public void femerConnexion()
    {
        database.close();
    }

    public SQLiteDatabase getDatabase(){
        return database;
    }

    public Vector<String> retournerInventions()
    {
        Vector<String> inventions = new Vector<>();
        Cursor requete = database.rawQuery("SELECT invention FROM inventeur", null);
        while(requete.moveToNext())
        {
            inventions.add(requete.getString(0));
        }
        return inventions;
    }

    public boolean aBonneReponse(String nom, String invention)
    {
        String[] tab = {nom.toLowerCase(), invention.toLowerCase()};
        Cursor requete = database.rawQuery("SELECT nom, invention FROM inventeur WHERE nom = ? AND invention = ?", tab);

//        while(requete.moveToNext()) //Fonctionne si on fait query qui prend toutes les rangées de nos colonnes
//        {
//            if(requete.getString(0).equals(nom) && requete.getString(1).equals(invention))
//            {
//                association = true;
//                break;
//            }
//        }
        return requete.moveToNext();
    }
}
