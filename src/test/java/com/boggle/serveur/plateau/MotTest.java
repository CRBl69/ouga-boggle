package com.boggle.serveur.plateau;

import static org.junit.Assert.assertTrue;

import com.boggle.serveur.dictionnaire.Dictionnaire;
import com.boggle.serveur.jeu.Langue;
import java.util.LinkedList;
import org.junit.Test;

public class MotTest {
    @Test
    public void creationMotExistant() {
        Dictionnaire.genererArbre(Langue.FR);
        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(new Lettre(new Coordonnee(0, 0), "t"));
        lettres.add(new Lettre(new Coordonnee(0, 1), "e"));
        lettres.add(new Lettre(new Coordonnee(0, 2), "s"));
        lettres.add(new Lettre(new Coordonnee(0, 3), "t"));
        try {
            new Mot(lettres);
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    public void creationMotNonExistant() {
        Dictionnaire.genererArbre(Langue.FR);
        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(new Lettre(new Coordonnee(0, 0), "t"));
        lettres.add(new Lettre(new Coordonnee(0, 1), "e"));
        lettres.add(new Lettre(new Coordonnee(0, 2), "s"));
        lettres.add(new Lettre(new Coordonnee(0, 3), "t"));
        lettres.add(new Lettre(new Coordonnee(0, 4), "t"));
        try {
            new Mot(lettres);
            assertTrue(false);
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void fonctionToString() {
        Dictionnaire.genererArbre(Langue.FR);
        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(new Lettre(new Coordonnee(0, 0), "t"));
        lettres.add(new Lettre(new Coordonnee(0, 1), "e"));
        lettres.add(new Lettre(new Coordonnee(0, 2), "s"));
        lettres.add(new Lettre(new Coordonnee(0, 3), "t"));
        try {
            new Mot(lettres).toString().equals("test");
            assertTrue(true);
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}
