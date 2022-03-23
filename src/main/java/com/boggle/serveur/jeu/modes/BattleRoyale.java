package com.boggle.serveur.jeu.modes;

import com.boggle.serveur.ServeurInterface;
import com.boggle.serveur.jeu.Joueur;
import com.boggle.serveur.jeu.Langue;
import com.boggle.serveur.jeu.Manche;

public class BattleRoyale extends Normal {

    public BattleRoyale (
            int nombreManche,
            int dureeManche,
            int tailleVerticale,
            int tailleHorizontale,
            Langue langue,
            ServeurInterface serveur) {
        super(nombreManche, dureeManche, tailleVerticale, tailleHorizontale, langue, serveur);
    }

    public void nouvelleManche(){
        Joueur perdant = null;
        int pointsPerdant = 0;
        for(var entry: super.getPoints().entrySet()) {
            var joueur = entry.getKey();
            var points = entry.getValue();
            if(perdant == null) {
                perdant = joueur;
                pointsPerdant = points;
            }
            else if(points < pointsPerdant) {
                pointsPerdant = points;
                perdant = joueur;
            }
        };
        super.enleverJoueur(perdant);
        super.nouvelleManche();
    }
}
