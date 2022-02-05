package com.boggle.serveur.plateau;

import com.boggle.serveur.jeu.Langue;

/** Plateau du jeu contenant les lettres. */
public class Grille {
    private Lettre[][] grille;
    private final Langue langue;

    public Grille (int size, Langue langue) {
        grille = new Lettre[size][size];
        this.langue = langue;
        GenerateurLettre gn = switch(langue) {
            case FR -> new GenerateurLettreFR();
            case EN -> throw new UnsupportedOperationException("Unimplemented case: " + langue);
        };

        genGrille(gn);
    }

    public void genGrille(GenerateurLettre gn) {
        for(int i = 0; i < grille.length; i++){
            for(int j = 0; j < grille[0].length; j++) {
                grille[i][j] = new Lettre(new Coordonnee(i, j), gn.prendreLettreAleatoire());
            }
        }
    }

}
