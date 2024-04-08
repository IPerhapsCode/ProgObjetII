package com.example.annexe8;

public class NegativeNumberException extends Exception{
    private double problemValue;

    public NegativeNumberException(double problemValue)
    {
        super("Le montant " + problemValue + " est négatif. Recommencez svp!");
        this.problemValue = problemValue;
    }
}
