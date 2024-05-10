package com.example.annexe15;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Vector;

public class Database extends SQLiteOpenHelper {

    private static Database instance;
    private SQLiteDatabase db;

    private Database(@Nullable Context context) {
        super(context, "db", null, 1);
    }

    public static Database getInstance(Context context)
    {
        if(instance == null)
        {
           instance = new Database(context);
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE EquipesLHJMQ(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nom TEXT, division TEXT, arena TEXT, capacite INTEGER);");

        Vector<String[]> equipes= new Vector<>(1, 1);
        equipes.add(new String[]{"Tigres de Victoriaville", "Est", "Colisée Desjardins", "1900"});
        equipes.add(new String[]{"Cataractes de Shawinigan", "Est", "Centre Gervais Auto", "4000"});
        equipes.add(new String[]{"Olympiques de Gatineau", "Ouest", "Centre Slush Puppie", "4200"});
        equipes.add(new String[]{"Foreurs de Val d’Or", "Ouest", "Centre Agnico Eagle", "2600"});
        equipes.add(new String[]{"Centre Agnico Eagle", "Ouest", "Centre Rousseau", "3000"});

        ContentValues cv = new ContentValues();
        for(String[] i : equipes)
        {
            cv.clear();
            cv.put("nom", i[0]);
            cv.put("division", i[1]);
            cv.put("arena", i[2]);
            cv.put("capacite", Integer.parseInt(i[3]));
            db.insert("EquipesLHJMQ", null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onCreate(db);
    }

    public void ouvrirConnexion()
    {
        this.db = instance.getWritableDatabase();
    }

    public void fermerConnexion()
    {
        this.db.close();
    }

    public int nbEquipeDivOuest()
    {
        try
        {
            Cursor c = this.db.rawQuery("SELECT COUNT(division) FROM EquipesLHJMQ WHERE division = ?;", new String[]{"Ouest"});
            c.moveToNext();
            return c.getInt(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public float moyenneCapaciteArena()
    {
        try
        {
            Cursor c = this.db.rawQuery("SELECT AVG(capacite) FROM EquipesLHJMQ;", null);
            c.moveToNext();
            return c.getFloat(0);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public Vector<String> getArenas()
    {
        Vector<String> arenas = new Vector<>(1, 1);
        Cursor c = null;

        try
        {
            c = this.db.rawQuery("SELECT arena FROM EquipesLHJMQ;", null);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        if(c != null)
        {
            while(c.moveToNext())
            {
                arenas.add(c.getString(0));
            }
        }

        return arenas;
    }

    public String getEquipe(String arena)
    {
        Cursor c = null;
        try
        {
            c = this.db.rawQuery("SELECT nom FROM EquipesLHJMQ WHERE arena = ? LIMIT 1;", new String[]{arena});
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        if(c != null)
        {
            c.moveToNext();
            return c.getString(0);
        }

        return "";
    }

    public Vector<Equipe> getEquipes()
    {
        Vector<Equipe> equipes = new Vector<>(1, 1);
        Cursor c = null;

        try
        {
            c = this.db.rawQuery("SELECT * FROM EquipesLHJMQ;", null);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        if(c != null)
        {
            while(c.moveToNext())
            {
                equipes.add(new Equipe(c.getString(1), c.getString(2), c.getString(3), c.getInt(4)));
            }
        }

        return equipes;
    }
}
