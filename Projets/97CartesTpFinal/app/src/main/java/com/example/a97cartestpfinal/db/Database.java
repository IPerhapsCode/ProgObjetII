package com.example.a97cartestpfinal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static Database instance;
    private SQLiteDatabase db;

    private Database(Context context)
    {
        super(context, "db", null, 1);
    }

    public static Database getInstance(Context context) {
        if(instance == null)
        {
            instance = new Database(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE highscore(_id INTEGER PRIMARY KEY AUTOINCREMENT, score INTEGER, nbCartes INTEGER, time TEXT);");
        db.execSQL("CREATE TABLE preference(valeur INTEGER);");
        db.execSQL("CREATE TABLE saved_game_info(score INTEGER, nbCartes INTEGER, time TEXT);");
        db.execSQL("CREATE TABLE saved_game_ordre_cartes(valeur INTEGER);");
        db.execSQL("CREATE TABLE saved_game_pile_main(_id INTEGER PRIMARY KEY, valeur INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onCreate(db);
    }

    public void ouvrirConnexion()
    {
        this.db = this.getWritableDatabase();
    }

    public void fermerConnexion()
    {
        try
        {
            this.db.close();
        }
        catch(NullPointerException npe)
        {
            System.out.println("La connexion à la base de donnée est fermer! Impossible de fermer la connexion!");
        }
    }

    public void saveHighscore(int score, int nbCartes, String time)
    {
        ContentValues values = new ContentValues();
        values.put("score", score);
        values.put("nbCartes", nbCartes);
        values.put("time", time);
        try
        {
            this.db.insert("highscore", null, values);
        }
        catch(NullPointerException npe)
        {
            System.out.println("La connexion à la base de donnée est fermer! Impossible de compléter l'insertion!");
        }
    }

    public int getHighestScore()
    {
        Cursor cursor = null;
        try
        {
            cursor = this.db.rawQuery("SELECT MAX(score) FROM highscore", null);
        }
        catch(NullPointerException npe)
        {
            System.out.println("La connexion à la base de donnée est fermer! Impossible d'obtenir le highscore!");
        }

        try
        {
            if(cursor.moveToNext())
            {
                return cursor.getInt(0);
            }
        }
        catch (NullPointerException npe)
        {
            System.out.println("Le curseur n'a pas pu être initialisé!");
        }

        return 0;
    }

    public Cursor getHighScores()
    {
        Cursor cursor = null;

        try
        {
            cursor = this.db.rawQuery("SELECT score, nbCartes, time FROM highscore", null);
        }
        catch (NullPointerException npe)
        {
            System.out.println("La connexion à la base de donnée est fermer! Impossible d'obtenir les highscores!");
        }

        return cursor;
    }
}
