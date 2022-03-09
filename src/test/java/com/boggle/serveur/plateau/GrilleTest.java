package com.boggle.serveur.plateau;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.boggle.serveur.jeu.Langue;
import org.junit.Test;

public class GrilleTest {
    @Test
    public void ajouterMot() {
        Grille grille = new Grille(6, 6, Langue.FR);

        Lettre[][] g = grille.getGrille();

        g[0][0] = new Lettre(new Coordonnee(0, 0), "p");
        g[0][1] = new Lettre(new Coordonnee(0, 1), "r");
        g[0][2] = new Lettre(new Coordonnee(0, 2), "e");
        g[0][3] = new Lettre(new Coordonnee(0, 3), "t");

        String mot = "pret";
        assertNotNull(grille.ajouterMot(mot));

        String mot2 = "" + grille.getGrille()[0][0] + grille.getGrille()[0][2] + grille.getGrille()[0][3]
                + grille.getGrille()[0][5];
        assertNull(grille.ajouterMot(mot2));

        String vide = "";
        assertNull(grille.ajouterMot(vide));
    }
}
