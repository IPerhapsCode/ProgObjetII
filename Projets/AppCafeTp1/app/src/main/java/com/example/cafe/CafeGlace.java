package com.example.cafe;

public class CafeGlace extends Produit{
    public CafeGlace(Tailles taille) {
        super(2.5, 10.0, taille);
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
