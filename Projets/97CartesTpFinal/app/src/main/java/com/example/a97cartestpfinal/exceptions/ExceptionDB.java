package com.example.a97cartestpfinal.exceptions;

//Exception lancée lors d'une erreur en relation avec la base de donnée
public class ExceptionDB extends Exception {
    public ExceptionDB(String specifier)
    {
        super("La connexion à la base de donnée est fermer! " + specifier);
    }
}
