package com.boggle.serveur.messages;

import com.boggle.serveur.plateau.Grille;

public class DebutManche {
    private String[][] tableau;
    private int longueurManche;

    public DebutManche(Grille grille, int lm) {
        tableau = new String[grille.getLignes()][grille.getColonnes()];
        for (int i = 0; i < grille.getLignes(); i++) {
            for (int j = 0; j < grille.getColonnes(); j++) {
                tableau[i][j] = grille.getGrille()[i][j].lettre;
            }
        }
        longueurManche = lm;
    }

    public DebutManche(String[][] grille, int lm) {
        tableau = grille;
        longueurManche = lm;
    }

    public String[][] getTableau() {
        return tableau;
    }

    public int getLongueurManche() {
        return longueurManche;
    }
}
