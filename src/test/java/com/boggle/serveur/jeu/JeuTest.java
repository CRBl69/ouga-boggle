package com.boggle.serveur.jeu;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import com.boggle.serveur.plateau.Coordonnee;
import com.boggle.serveur.plateau.Grille;
import com.boggle.serveur.plateau.Lettre;
import com.boggle.serveur.plateau.Mot;
import com.boggle.serveur.jeu.Langue;

import org.junit.Test;

public class JeuTest {
    @Test
    public void creationDeJeu() {
        new Jeu(1, new Grille(4, 4, Langue.FR));

        try {
            new Jeu(-1, new Grille(4, 4, Langue.FR));
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void ajouterJoueur() {
        Jeu jeu = new Jeu(1, new Grille(4, 4, Langue.FR));

        jeu.ajouterJoueur(new Joueur("Bogdan"));
        assertTrue(jeu.getJoueurs().size() == 1);

        jeu.ajouterJoueur(new Joueur("Claire"));
        assertTrue(jeu.getJoueurs().size() == 2);
    }

    @Test
    public void ajouterMotTrouve() {
        Jeu jeu = new Jeu(1, new Grille(4, 4, Langue.FR));
        Joueur joueur = new Joueur("Bogdan");
        jeu.ajouterJoueur(joueur);

        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(new Lettre(new Coordonnee(0, 0), "b"));
        lettres.add(new Lettre(new Coordonnee(0, 1), "i"));
        lettres.add(new Lettre(new Coordonnee(0, 2), "t"));
        lettres.add(new Lettre(new Coordonnee(0, 3), "e"));
        Mot mot = new Mot(lettres);

        jeu.ajouterMotTrouve(mot, joueur);

        assertTrue(jeu.getPoints().get(joueur) == 1);
    }

    @Test
    public void partieFinie() {
        Jeu jeu = new Jeu(1, new Grille(4, 4, Langue.FR));
        Joueur joueur1 = new Joueur("Bogdan");
        Joueur joueur2 = new Joueur("Claire");
        jeu.ajouterJoueur(joueur1);
        jeu.ajouterJoueur(joueur2);

        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(new Lettre(new Coordonnee(0, 0), "b"));
        lettres.add(new Lettre(new Coordonnee(0, 1), "i"));
        lettres.add(new Lettre(new Coordonnee(0, 2), "t"));
        lettres.add(new Lettre(new Coordonnee(0, 3), "e"));
        Mot mot = new Mot(lettres);

        jeu.ajouterMotTrouve(mot, joueur1);

        assertTrue(jeu.getJoueursGagnants().size() == 1);
        assertTrue(jeu.getJoueursGagnants().get(0).equals(joueur1));
    }
}
