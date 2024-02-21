package com.example.cafe;

public class Americano extends Produit{
    Americano(Tailles taille)
    {
        super(2.4, 9.0, taille);
    }

    @Override
    protected double findPrixMoyen(double prixPetit) {
        return 5/3 * prixPetit;
    }

    @Override
    protected double findPrixGrand(double prixPetit) {
        return 2.2 * prixPetit;
    }

    @Override
    protected double findCaloriesMoyen(double caloriesPetit) {
        return 2 + caloriesPetit;
    }

    @Override
    protected double findCaloriesGrand(double caloriesPetit) {
        return 2 * caloriesPetit;
    }
}
