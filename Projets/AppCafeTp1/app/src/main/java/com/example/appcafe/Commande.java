package com.example.appcafe;

import com.example.cafe.Produit;

import java.util.Vector;

public class Commande {
    private final double taxes = 1.14975;
    private Vector<Produit> commande;
    private double total;

    public Commande(){
        this.commande = new Vector();
        this.total = 0;
    }

    public void ajouterProduit(Produit produit)
    {
        this.commande.add(produit);
        this.total += this.commande.lastElement().getPrix() * this.taxes;
    }

    public Vector<Produit> getCommande() {
        return commande;
    }

    public double getTotal() {
        return total;
    }
}
