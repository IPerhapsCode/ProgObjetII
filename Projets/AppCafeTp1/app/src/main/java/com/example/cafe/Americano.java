package com.example.cafe;

public class Americano extends Produit{
    Americano(Tailles taille)
    {
        super("Americano",2.4, 9.0, taille);
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
        return 2.0 + caloriesPetit;
    }

    @Override
    protected double findCaloriesGrand(double caloriesPetit) {
        return 2.0 * caloriesPetit;
    }
}
