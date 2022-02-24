package com.boggle.serveur.jeu;

import java.io.Serializable;
import java.util.HashSet;

import com.boggle.serveur.plateau.Mot;

public class Historique implements Serializable{
    private Joueur joueur; 
    private int points;
    private HashSet<Mot> mots;
    
    public Historique(Joueur joueur, int points, HashSet<Mot> mots) {
        this.joueur = joueur;
        this.points = points;
        this.mots = mots;
    }

    public Joueur getJoueur() {
        return joueur;
    }
    
    public int getPoints() {
        return points;
    }

    public HashSet<Mot> getMots() {
        return mots;
    }
}
