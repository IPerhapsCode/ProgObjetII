package com.example.annexe3_exercices;

import java.util.Vector;

public class Commande {

    private Vector<Produit> listeCommande;

    public Commande ( )
    {
        listeCommande = new Vector();
    }

    public void ajouterProduit ( Produit p )
    {
        listeCommande.add(p);
    }

    public double total ()
    {
        double total =0;
          // compléter : total de la commande
            for (Produit i: listeCommande)
            {
                total += i.getPrixUnitaire() * i.getQte();
            }

        return total;
    }

    public double taxes()
    {
        double taxes = 0;
        double tps = 0.05;
        double tvq = 0.09975;
        double montant = this.total();
  
	// compléter : montant des taxes sur le total de la commande

        // tps sur le montant avant taxes ( 5% )
        
        //tvq sur le montant avant taxes ( 9.975% )
        
        // taxes total = tps + tvq

	    taxes += montant * tps;
        taxes += montant * tvq;

        return taxes;
    }

    public double grandTotal()
    {
	
        double grTotal = 0;

        // compléter
        grTotal += this.total() + this.taxes();

        return grTotal;
	

    }
}
