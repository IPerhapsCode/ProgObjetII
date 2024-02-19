package com.example.premiereapplication;

public class Compte {
    private double solde;
    private String nom;

    Compte(double solde, String nom)
    {
        this.solde = solde;
        this.nom = nom;
    }

    public double getSolde() {
        return solde;
    }

    public void transfert(double transfert) {
        this.solde -= transfert;
    }

    public String getNom() {
        return nom;
    }
}
