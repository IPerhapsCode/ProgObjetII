package com.example.pratiqueexamen1;

import java.util.Vector;

public class Saison {
    private Vector<Entrainement> liste;
    public static final int OBJECTIF = 12;

    public Saison()
    {
        this.liste = new Vector<>(1,1);
    }

    public void addEntrainement(Entrainement entrainement)
    {
        this.liste.add(entrainement);
    }

    public int nbHeuresEntrainees()
    {
        int nbHeures = 0;
        for(Entrainement i: this.liste)
        {
            nbHeures += i.getFin() - i.getDébut();
        }
        return nbHeures;
    }
}
