package com.example.pratiqueexamen1;

import java.time.LocalTime;

public class Entrainement {
    private String type;
    private int début;
    private int fin;

    public Entrainement(String type, int début, int fin) {
        this.type = type;
        this.début = début;
        this.fin = fin;
    }

    public String getType() {
        return type;
    }

    public int getDébut() {
        return début;
    }

    public int getFin() {
        return fin;
    }
}
