package com.boggle.serveur.jeu;

/** Contient les informations relatives au joueur. */
public class Joueur {
    public final String nom;
    public boolean estPret = false;

    public Joueur(String nom) {
        this.nom = nom;
    }

    public String toString() {
        return nom;
    }

    public int hashCode() {
        return nom.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof Joueur) {
            Joueur j = (Joueur) o;
            return nom.equals(j.nom);
        }
        return false;
    }
}
