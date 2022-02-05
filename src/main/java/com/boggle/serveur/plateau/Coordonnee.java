package com.boggle.serveur.plateau;

public class Coordonnee {
    public final int x;
    public final int y;

    public Coordonnee (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean estACoteDe (Coordonnee coord) {
        return Math.abs(this.x - coord.x) < 2 && Math.abs(this.y - coord.y) < 2;
    }
}