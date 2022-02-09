package com.boggle.serveur.plateau;

public class Coordonnee {
    public final int x;
    public final int y;

    /**
     * Constructeur.
     * 
     * @param x l'emplacement dans les colonnes. 
     * @param y l'emplacement dans les lignes.
     */
    public Coordonnee (int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Vérifie si deux coordonnées sont adjacentes.
     * 
     * @param coord coordonnées d'un emplacement
     * @return boolean true si les deux coordonnées sont adjacentes.
     */
    public boolean estACoteDe (Coordonnee coord) {
        return Math.abs(this.x - coord.x) < 2 && Math.abs(this.y - coord.y) < 2;
    }
}