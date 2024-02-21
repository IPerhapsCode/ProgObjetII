package com.example.cafe;

import java.util.Hashtable;

public class ListeProduits {

    private Hashtable<String, Produit> liste;

    public ListeProduits ()
    {
        liste = new Hashtable();
        liste.put("Café filtre Petit",new CafeFiltre(Produit.Tailles.petit));
        liste.put("Café filtre Moyen",new CafeFiltre(Produit.Tailles.moyen));
        liste.put("Café filtre Grand",new CafeFiltre(Produit.Tailles.grand));
        liste.put("Americano Petit",new Americano(Produit.Tailles.petit));
        liste.put("Americano Moyen",new Americano(Produit.Tailles.moyen));
        liste.put("Americano Grand",new Americano(Produit.Tailles.grand));
        liste.put("Café glacé Petit",new CafeGlace(Produit.Tailles.petit));
        liste.put("Café glacé Moyen",new CafeGlace(Produit.Tailles.moyen));
        liste.put("Café glacé Grand",new CafeGlace(Produit.Tailles.grand));
        liste.put("Latté Petit",new Latte(Produit.Tailles.petit));
        liste.put("Latté Moyen",new Latte(Produit.Tailles.moyen));
        liste.put("Latté Grand",new Latte(Produit.Tailles.grand));
    }

    public Produit recupererProduit ( String cle)
    {
       return liste.get(cle);
    }


}
