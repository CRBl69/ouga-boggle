package com.boggle.serveur.jeu;

import java.util.ArrayList;

public class PartieMultijoueur extends Partie {
    private ArrayList<Joueur> joueurs;

    public PartieMultijoueur(Manche manche, ArrayList<Joueur> joueurs) {
        super(manche);
        this.joueurs = joueurs;
    }

    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }
}
