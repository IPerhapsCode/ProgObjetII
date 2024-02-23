package com.example.cafe;

public class Latte extends Produit{

    public Latte(Tailles taille) {
        super("Latté",4.0, 125.0, taille);
    }

    @Override
    protected double findPrixMoyen(double prixPetit) {
        return (5.0/3.0) * prixPetit;
    }

    @Override
    protected double findPrixGrand(double prixPetit) {
        return 2.5 * prixPetit;
    }

    @Override
    protected double findCaloriesMoyen(double caloriesPetit) {
        return (5.0/3.0) * caloriesPetit;
    }

    @Override
    protected double findCaloriesGrand(double caloriesPetit) {
        return 2.0 * caloriesPetit;
    }
}
