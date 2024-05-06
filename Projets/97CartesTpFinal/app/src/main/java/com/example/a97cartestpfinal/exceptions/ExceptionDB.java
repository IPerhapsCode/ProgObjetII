package com.example.a97cartestpfinal.exceptions;

public class ExceptionDB extends Exception {
    public ExceptionDB(String specifier)
    {
        super("La connexion à la base de donnée est fermer! " + specifier);
    }
}
