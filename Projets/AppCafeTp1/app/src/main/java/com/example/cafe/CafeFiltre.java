package com.example.cafe;

//Je ne peux pas utiliser une variable pour stocker les valeurs petit pis c'est désagréable
public class CafeFiltre extends Produit {
    public CafeFiltre(Tailles taille) {
        super("Café Filtre",1.8, 5.0, taille);
    }

    @Override
    protected double findPrixMoyen(double prixPetit) {
        return (5.0/3.0) * prixPetit;
    }

    @Override
    protected double findPrixGrand(double prixPetit) {
        return 2.2 * prixPetit;
    }

    @Override
    protected double findCaloriesMoyen(double caloriesPetit) {
        return 7.0;
    }

    @Override
    protected double findCaloriesGrand(double caloriesPetit) {
        return 2.0 * caloriesPetit;
    }
}
