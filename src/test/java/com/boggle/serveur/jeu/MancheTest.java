package com.boggle.serveur.jeu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.boggle.serveur.plateau.Coordonnee;
import com.boggle.serveur.plateau.Lettre;
import java.util.LinkedList;
import org.junit.Test;

public class MancheTest {
    @Test
    public void creationDeManche() {
        new Manche(4, 4, 60, Langue.FR);
        assertTrue(true);
    }

    @Test
    public void ajouterMotEtGetPoints() {
        Manche manche = new Manche(4, 4, 60, Langue.FR);
        Joueur joueur = new Joueur("Bogdan");

        LinkedList<Lettre> lettres = new LinkedList<>();
        lettres.add(new Lettre(new Coordonnee(0, 0), "t"));
        lettres.add(new Lettre(new Coordonnee(0, 1), "e"));
        lettres.add(new Lettre(new Coordonnee(0, 2), "s"));
        lettres.add(new Lettre(new Coordonnee(0, 3), "t"));

        manche.ajouterMotTrouve(lettres, joueur);

        assertEquals(1, (int) (manche.getPoints().get(joueur)));
    }
}
