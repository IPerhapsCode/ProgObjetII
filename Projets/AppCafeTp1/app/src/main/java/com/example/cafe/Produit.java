package com.example.cafe;

public abstract class Produit {
    public enum Tailles{
        petit,
        moyen,
        grand
    }
    private double prix;
    private double prixPetit;
    private double calories;
    private double caloriesPetit;
    private Tailles taille;

    public Produit(Double prixPetit, Double caloriesPetit, Tailles taille)
    {
        this.prixPetit = prixPetit;
        this.caloriesPetit = caloriesPetit;

        this.taille = taille;
        switch(this.taille){
            case petit:{
                this.prix = this.prixPetit;
                this.calories = this.caloriesPetit;
            }
            case moyen:{
                this.prix = this.findPrixMoyen(this.prixPetit);
                this.calories = this.findCaloriesMoyen(this.caloriesPetit);
            }
            case grand:{
                this.prix = this.findPrixGrand(this.prixPetit);
                this.calories = this.findCaloriesGrand(this.caloriesPetit);
            }
        }
    }

    abstract protected double findPrixMoyen(double prixPetit);

    abstract protected double findPrixGrand(double prixPetit);

    abstract protected double findCaloriesMoyen(double caloriesPetit);

    abstract protected double findCaloriesGrand(double caloriesPetit);

    public double getPrix() {
        return prix;
    }

    public double getCalories() {
        return calories;
    }
}

//    //Associe chaque taille à un nombre de calories ou à un prix (Finalement cette fonction est inutile je suis juste pissed qu'elle est servit à rien) :(
//    protected void setListe(Hashtable<Tailles, Double> banqueValeur, Double... valeurs)
//    {
//        if(valeurs.length == Tailles.values().length)
//        {
//            int iteration = 0;
//
//            for(Tailles i : Tailles.values())
//            {
//                banqueValeur.put(i, valeurs[iteration]);
//                ++iteration;
//            }
//        }
//        else
//        {
//            System.out.println("Le nombre de valeurs n'est pas égale au nombre de tailles possibles!");
//        }
//    }
