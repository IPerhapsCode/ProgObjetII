package com.example.a97cartestpfinal.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.LinearLayout;

import com.example.a97cartestpfinal.exceptions.ExceptionDB;
import com.example.a97cartestpfinal.logique.Cartes;
import com.example.a97cartestpfinal.logique.Partie;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

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

    public boolean ouvrirConnexion()
    {
        this.db = this.getWritableDatabase();
        return true;
    }

    public boolean fermerConnexion() throws ExceptionDB {
        try
        {
            this.db.close();
        }
        catch(NullPointerException npe)
        {
            throw new ExceptionDB("Impossible de fermer la connexion!");
        }
        return false;
    }

    public void saveHighscore(int score, int nbCartes, String time, boolean saved) throws ExceptionDB {
        ContentValues values = new ContentValues();
        values.put("score", score);
        values.put("nbCartes", nbCartes);
        values.put("time", time);
        try
        {
            this.db.insert("highscore", null, values);
            //Clears saved games once a highscore is saved while the saved game was loaded
            if(saved)
            {
                this.db.rawQuery("DELETE FROM saved_game_info", null);
                this.db.rawQuery("DELETE FROM saved_game_ordre_cartes", null);
                this.db.rawQuery("DELETE FROM saved_game_pile_main", null);
            }
        }
        catch(NullPointerException npe)
        {
            throw new ExceptionDB("Impossible de compléter l'insertion!");
        }
    }

    public int getHighestScore() throws ExceptionDB {
        Cursor cursor = null;
        try
        {
            cursor = this.db.rawQuery("SELECT MAX(score) FROM highscore ORDER BY nbCartes ASC LIMIT 1", null);
        }
        catch(NullPointerException npe)
        {
            throw new ExceptionDB("Impossible d'obtenir le highscore!");
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
            throw new ExceptionDB("Le curseur n'a pas pu être initialisé!");
        }

        return 0;
    }

    public Cursor getHighScores() throws ExceptionDB {
        Cursor cursor = null;

        try
        {
            cursor = this.db.rawQuery("SELECT score, nbCartes, time FROM highscore ORDER BY nbCartes ASC, score DESC", null);
        }
        catch (NullPointerException npe)
        {
            throw new ExceptionDB("Impossible d'obtenir les highscores!");
        }

        return cursor;
    }

    public void saveGame(int score, int nbCartes, String time, List cartesValues, Collection cartes, Collection pilesMain) throws ExceptionDB {
        try
        {
            this.deleteSavedGame();
        }
        catch(ExceptionDB e)
        {
            System.out.println(e.getMessage());
        }

        try
        {
            ContentValues cv = new ContentValues();
            cv.put("score", score);
            cv.put("nbCartes", nbCartes);
            cv.put("time", time);
            this.db.insert("saved_game_info", null, cv);
        }
        catch(NullPointerException npe)
        {
            throw new ExceptionDB("Impossible d'insérer des valeurs dans la table saved_game_info!");
        }

        try
        {
            ContentValues cv = new ContentValues();

            for(Object i : cartesValues)
            {
                cv.clear();
                cv.put("valeur", (int) i);
                this.db.insert("saved_game_ordre_cartes", null, cv);
            }

        }
        catch(NullPointerException npe)
        {
            throw new ExceptionDB("Impossible d'insérer des valeurs dans la table saved_game_ordre_cartes!");
        }

        try
        {
            ContentValues cv = new ContentValues();
            String count = "";

            for(Object i : pilesMain)
            {
                count = ((LinearLayout) ((Cartes) i).getCarte().getParent()).getTag().toString();
                cv.clear();
                cv.put("_id", Integer.parseInt(count.charAt(count.length() - 2) + String.valueOf(count.charAt(count.length() - 1))));
                cv.put("valeur", ((Cartes) i).getValue());
                this.db.insert("saved_game_pile_main", null, cv);
            }

            for(Object i : cartes)
            {
                count = ((LinearLayout) ((Cartes) i).getCarte().getParent()).getTag().toString();
                cv.clear();
                cv.put("_id", Integer.parseInt(count.charAt(count.length() - 2) + String.valueOf(count.charAt(count.length() - 1))));
                cv.put("valeur", ((Cartes) i).getValue());
                this.db.insert("saved_game_pile_main", null, cv);
            }
        }
        catch(NullPointerException npe)
        {
            throw new ExceptionDB("Impossible d'insérer des valeurs dans la table saved_game_pile_main!");
        }
    }

    public void loadGame(Partie partie) throws ExceptionDB {
        Cursor c = null;
        try
        {
            c = this.db.rawQuery("SELECT * FROM saved_game_info", null);
        }
        catch(NullPointerException npe)
        {
            throw new ExceptionDB("Impossible de sélectionner les valeurs de la table saved_game_info!");
        }

        if(c != null)
        {
            c.moveToNext();
            for(int i = 0; i < 3; ++i)
            {
                switch(i)
                {
                    case 0:{
                        partie.setScore(c.getInt(i));
                        break;
                    }
                    case 1:{
                        partie.setNbCartes(c.getInt(i) - partie.getCarteMaxValue());
                        partie.setCount(partie.getCarteMaxValue() - c.getInt(i));
                        break;
                    }
                    case 2:{
                        partie.setBaseTime(c.getString(i));
                        break;
                    }
                }
            }
            c = null;
        }

        try
        {
            c = this.db.rawQuery("SELECT * FROM saved_game_ordre_cartes", null);
        }
        catch(NullPointerException npe)
        {
            throw new ExceptionDB("Impossible de sélectionner les valeurs de la table saved_game_ordre_cartes!");
        }

        if(c != null)
        {
            while(c.moveToNext())
            {
                partie.getCarteValues().add(c.getInt(0));
            }
            c = null;
        }

        try
        {
            c = this.db.rawQuery("SELECT * FROM saved_game_pile_main", null);
        }
        catch(NullPointerException npe)
        {
            throw new ExceptionDB("Impossible de sélectionner les valeurs de la table saved_game_pile_main!");
        }

        if(c != null)
        {
            partie.setSavedCartes(new Hashtable<>(1, 1));

            while(c.moveToNext())
            {
                partie.getSavedCartes().put(c.getInt(0), c.getInt(1));
            }
        }
    }

    public boolean hasSavedGame() throws ExceptionDB {
        try
        {
            Cursor c = this.db.rawQuery("SELECT COUNT(*) FROM saved_game_info", null);
            c.moveToNext();
            if(c.getInt(0) > 0)
            {
                return true;
            }
        }
        catch (NullPointerException npe)
        {
            throw new ExceptionDB("Impossible de compter le nombre de ligne dans saved_game_info!");
        }

        return false;
    }

    public void deleteSavedGame() throws ExceptionDB {
        try
        {
            this.db.execSQL("DELETE FROM saved_game_info;");
            this.db.execSQL("DELETE FROM saved_game_ordre_cartes");
            this.db.execSQL("DELETE FROM saved_game_pile_main");
        }
        catch(NullPointerException npe)
        {
            throw new ExceptionDB("Impossible de vider les tables!");
        }
    }
}

