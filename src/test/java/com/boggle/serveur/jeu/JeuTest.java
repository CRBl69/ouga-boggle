package com.boggle.serveur.jeu;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import com.boggle.serveur.plateau.Grille;
import com.boggle.serveur.plateau.Lettre;
import com.boggle.serveur.plateau.Mot;

import org.junit.Test;

public class JeuTest {
    @Test
    public void creationDeJeu() {
        new Jeu(1, new Grille());

        try {
            new Jeu(-1, new Grille());
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    @Test
    public void ajouterJoueur() {
        Jeu jeu = new Jeu(1, new Grille());

        jeu.ajouterJoueur(new Joueur("Bogdan"));
        assertTrue(jeu.getJoueurs().size() == 1);

        jeu.ajouterJoueur(new Joueur("Claire"));
        assertTrue(jeu.getJoueurs().size() == 2);
    }

    @Test
    public void ajouterMotTrouve() {
        Jeu jeu = new Jeu(1, new Grille());
        Joueur joueur = new Joueur("Bogdan");
        jeu.ajouterJoueur(joueur);

        // TODO: mettre à jour lorsque les mots seront implémentés
        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(new Lettre());
        lettres.add(new Lettre());
        lettres.add(new Lettre());
        lettres.add(new Lettre());
        Mot mot = new Mot(lettres);
        jeu.ajouterMotTrouve(mot, joueur);
        assertTrue(jeu.getPoints().get(joueur) == 1);
    }

    @Test
    public void partieFinie() {
        Jeu jeu = new Jeu(1, new Grille());
        Joueur joueur1 = new Joueur("Bogdan");
        Joueur joueur2 = new Joueur("Claire");
        jeu.ajouterJoueur(joueur1);
        jeu.ajouterJoueur(joueur2);

        // TODO: mettre à jour lorsque les mots seront implémentés
        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(new Lettre());
        lettres.add(new Lettre());
        lettres.add(new Lettre());
        lettres.add(new Lettre());
        Mot mot = new Mot(lettres);
        jeu.ajouterMotTrouve(mot, joueur1);

        assertTrue(jeu.getJoueursGagnants().size() == 1);
        assertTrue(jeu.getJoueursGagnants().get(0).equals(joueur1));
    }
}
