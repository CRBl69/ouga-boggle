package com.boggle.serveur.messages;

import com.boggle.serveur.plateau.Grille;

public class DebutManche {
    private String[][] tableau;

    public DebutManche(Grille grille) {
        tableau = new String[grille.getLignes()][grille.getColonnes()];
        for (int i = 0; i < grille.getLignes(); i++) {
            for (int j = 0; j < grille.getColonnes(); j++) {
                tableau[i][j] = grille.getGrille()[i][j].lettre;
            }
        }
    }

    public String[][] getTableau() {
        return tableau;
    }
}
