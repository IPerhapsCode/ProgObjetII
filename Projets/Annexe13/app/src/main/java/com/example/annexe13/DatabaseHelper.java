package com.example.annexe13;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

public class DatabaseHelper extends SQLiteOpenHelper {

    static private DatabaseHelper instance;
    private SQLiteDatabase database;

    private DatabaseHelper(Context context) {
        super(context, "db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE bieres(nom TEXT, microbrasserie TEXT, evaluation REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void enregistrerBiere(Evaluation eval) throws Exception {
        if(database != null)
        {
            ContentValues cv = new ContentValues();
            cv.put("nom", eval.getNom());
            cv.put("microbrasserie", eval.getMicrobrasserie());
            cv.put("evaluation", eval.getEvaluation());
            database.insert("bieres", null, cv);
        }
        else
        {
            throw new Exception("The database is not currently opened!");
        }
    }

    public Vector voirTopTrois() throws Exception {
        Vector<String> v = new Vector<>(1, 1);
        if(database != null)
        {
            Cursor c = database.rawQuery("SELECT nom FROM bieres ORDER BY evaluation DESC LIMIT 3", null);

            while(c.moveToNext())
            {
                v.add(c.getString(0));
            }
        }
        else
        {
            throw new Exception("The database hasn't been opened!");
        }

        if(v.size() < 3)
        {
            throw new Exception("There aren't enough beers to create a leaderboard (minimum of 3)!");
        }

        return v;
    }

    public void ouvrirConnection()
    {
        database = this.getWritableDatabase();
    }

    public void fermerConnection()
    {
        try
        {
            database.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static DatabaseHelper getInstance(Context context) {
        if(instance == null)
        {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }
}
