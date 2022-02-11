package com.boggle.serveur.jeu;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import com.boggle.serveur.plateau.Coordonnee;
import com.boggle.serveur.plateau.Grille;
import com.boggle.serveur.plateau.Lettre;

import org.junit.Test;

public class JeuTest {
    @Test
    public void creationDeJeu() {
        new Jeu(1, new Grille(4, 4, Langue.FR), new Minuteur(30));

        try {
            new Jeu(-1, new Grille(4, 4, Langue.FR), new Minuteur(30));
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void ajouterJoueur() {
        Jeu jeu = new Jeu(1, new Grille(4, 4, Langue.FR), new Minuteur(30));

        jeu.ajouterJoueur(new Joueur("Bogdan"));
        assertTrue(jeu.getJoueurs().size() == 1);

        jeu.ajouterJoueur(new Joueur("Claire"));
        assertTrue(jeu.getJoueurs().size() == 2);
    }

    @Test
    public void ajouterMotTrouve() {
        Jeu jeu = new Jeu(1, new Grille(4, 4, Langue.FR), new Minuteur(30));
        Joueur joueur = new Joueur("Bogdan");
        jeu.ajouterJoueur(joueur);

        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(new Lettre(new Coordonnee(0, 0), "t"));
        lettres.add(new Lettre(new Coordonnee(0, 1), "e"));
        lettres.add(new Lettre(new Coordonnee(0, 2), "s"));
        lettres.add(new Lettre(new Coordonnee(0, 3), "t"));

        jeu.ajouterMotTrouve(lettres, joueur);

        assertTrue(jeu.getPoints().get(joueur) == 1);
    }

    @Test
    public void partieFinie() {
        Jeu jeu = new Jeu(1, new Grille(4, 4, Langue.FR), new Minuteur(30));
        Joueur joueur1 = new Joueur("Bogdan");
        Joueur joueur2 = new Joueur("Claire");
        jeu.ajouterJoueur(joueur1);
        jeu.ajouterJoueur(joueur2);

        assertTrue(jeu.getJoueurGagnant() == null);
        
        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(new Lettre(new Coordonnee(0, 0), "t"));
        lettres.add(new Lettre(new Coordonnee(0, 1), "e"));
        lettres.add(new Lettre(new Coordonnee(0, 2), "s"));
        lettres.add(new Lettre(new Coordonnee(0, 3), "t"));

        jeu.ajouterMotTrouve(lettres, joueur1);
        assertTrue(jeu.getJoueurGagnant().equals(joueur1));
    }
}
