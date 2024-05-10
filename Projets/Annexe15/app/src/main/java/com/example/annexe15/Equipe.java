package com.example.annexe15;

public class Equipe {
    public static enum Div{Ouest, Est};
    private String nom;
    private String arena;
    private Div division;
    private int capacite;

    public Equipe(String nom, String arena, String division, int capacite)
    {
        this.nom = nom;
        this.arena = arena;

        switch(division)
        {
            case "Ouest": this.division = Div.Ouest; break;
            default: this.division = Div.Est; break;
        }

        this.capacite = capacite;
    }

    public String getNom() {
        return nom;
    }
}
