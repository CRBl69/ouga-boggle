package com.boggle.serveur.plateau;

import com.boggle.serveur.jeu.Langue;

/** Plateau du jeu contenant les lettres. */
public class Grille {
    private Lettre[][] grille;
    private final GenerateurLettre langue;

    /**
     * Constructeur.
     * 
     * @param colonne nombre de colonnes de la grille
     * @param ligne nombre de lignes de la grille
     * @param langue la langue choisie
     */
    public Grille (int colonne, int ligne, Langue langue) {
        if(colonne < 1 || ligne < 1)
            throw new IllegalArgumentException();
        grille = new Lettre[colonne][ligne];
        this.langue = switch(langue) {
            case FR -> new GenerateurLettreFR();
            case EN -> throw new UnsupportedOperationException("Unimplemented case: " + langue);
        };
        genGrille();
    }

    /**
     * Génère la grille aléatoirement.
     * 
     */
    public void genGrille() {
        for(int i = 0; i < grille.length; i++){
            for(int j = 0; j < grille[0].length; j++) {
                grille[i][j] = new Lettre(new Coordonnee(i, j), langue.prendreLettreAleatoire());
            }
        }
    }
}
