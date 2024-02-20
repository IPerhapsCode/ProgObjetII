package entrez votre nom de package;

import java.util.Hashtable;

public class ListeProduits {

    private Hashtable<String, Produit> liste;

    public ListeProduits ()
    {
        liste = new Hashtable();
        liste.put("Café filtre Petit",/* à compléter*/ );
        liste.put("Café filtre Moyen",/* à compléter */);
        liste.put("Café filtre Grand", /* à compléter*/);
        liste.put("Americano Petit",/* à compléter*/);
        liste.put("Americano Moyen", );
        liste.put("Americano Grand", );
        liste.put("Café glacé Petit", );
        liste.put("Café glacé Moyen", );
        liste.put("Café glacé Grand", );
        liste.put("Latté Petit", );
        liste.put("Latté Moyen", );
        liste.put("Latté Grand", );
    }

    public Produit recupererProduit ( String cle)
    {
       // à compléter
    }


}
