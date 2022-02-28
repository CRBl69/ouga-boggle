package com.boggle.serveur.jeu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.boggle.serveur.plateau.Coordonnee;
import com.boggle.serveur.plateau.Lettre;
import java.util.LinkedList;
import org.junit.Test;

public class JeuTest {
    @Test
    public void creationDeJeu() {
        new Jeu(3, 60, 4, 4, Langue.FR);

        try {
            new Jeu(-1, -1, -1, -1, Langue.FR);
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void ajouterJoueur() {
        Jeu jeu = new Jeu(3, 60, 4, 4, Langue.FR);

        jeu.ajouterJoueur(new Joueur("Bogdan"));
        assertTrue(jeu.getJoueurs().size() == 1);

        jeu.ajouterJoueur(new Joueur("Claire"));
        assertEquals(2, jeu.getJoueurs().size());
    }

    @Test
    public void ajouterMotTrouve() {
        Jeu jeu = new Jeu(3, 60, 4, 4, Langue.FR);
        Joueur joueur = new Joueur("Bogdan");
        jeu.ajouterJoueur(joueur);

        jeu.commencerPartie();

        var grille = jeu.getMancheCourante().getGrille().getGrille();

        grille[0][0] = new Lettre(new Coordonnee(0, 0), "p");
        grille[0][1] = new Lettre(new Coordonnee(0, 1), "r");
        grille[0][2] = new Lettre(new Coordonnee(0, 2), "e");
        grille[0][3] = new Lettre(new Coordonnee(0, 3), "t");

        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(jeu.getMancheCourante().getGrille().getGrille()[0][0]);
        lettres.add(jeu.getMancheCourante().getGrille().getGrille()[0][1]);
        lettres.add(jeu.getMancheCourante().getGrille().getGrille()[0][2]);
        lettres.add(jeu.getMancheCourante().getGrille().getGrille()[0][3]);
        System.out.println(lettres.toString());
        jeu.ajouterMotTrouve(lettres, joueur);

        assertEquals(1, (int) (jeu.getPoints().get(joueur)));
    }

    @Test
    public void joueurGagnants() {
        Jeu jeu = new Jeu(3, 60, 4, 4, Langue.FR);
        Joueur joueur1 = new Joueur("Bogdan");
        Joueur joueur2 = new Joueur("Claire");
        jeu.ajouterJoueur(joueur1);
        jeu.ajouterJoueur(joueur2);

        jeu.commencerPartie();

        assertEquals(2, jeu.getJoueurGagnant().size());

        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(new Lettre(new Coordonnee(0, 0), "t"));
        lettres.add(new Lettre(new Coordonnee(0, 1), "e"));
        lettres.add(new Lettre(new Coordonnee(0, 2), "s"));
        lettres.add(new Lettre(new Coordonnee(0, 3), "t"));

        jeu.ajouterMotTrouve(lettres, joueur1);
        assertEquals(joueur1, jeu.getJoueurGagnant().get(0));
    }
}
