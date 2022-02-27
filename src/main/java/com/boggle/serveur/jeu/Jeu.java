package com.boggle.serveur.jeu;

import com.boggle.serveur.plateau.Lettre;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/** Fonctions relatives à la partie. */
public class Jeu {
    private HashSet<Joueur> joueurs;
    private ArrayList<Manche> manches;

    // Parametres
    private int nombreMancheTotal;
    private int dureeManche;
    private int tailleVerticale;
    private int tailleHorizontale;
    private Langue langue;

    public Jeu(int nombreManche, int dureeManche, int tailleVerticale, int tailleHorizontale, Langue langue) {
        this.joueurs = new HashSet<>();
        this.manches = new ArrayList<>();
        this.nombreMancheTotal = nombreManche;
        this.dureeManche = dureeManche;
        this.tailleHorizontale = tailleHorizontale;
        this.tailleVerticale = tailleVerticale;
        this.langue = langue;
        if (nombreManche < 1) {
            throw new IllegalArgumentException("La nombre de manches doit être supérieur ou égal à 1.");
        } else {
            this.nombreMancheTotal = nombreManche;
        }
        if (dureeManche < 0) {
            throw new IllegalArgumentException("La durée de la manche doit être supérieure ou égal à 0.");
        } else {
            this.dureeManche = dureeManche;
        }
        if (tailleHorizontale < 1) {
            throw new IllegalArgumentException("La taille horizontale doit être supérieure ou égal à 1.");
        } else {
            this.tailleHorizontale = tailleHorizontale;
        }
        if (tailleVerticale < 1) {
            throw new IllegalArgumentException("La taille verticale doit être supérieure ou égal à 1.");
        } else {
            this.tailleVerticale = tailleVerticale;
        }
    }

    public void commencerPartie() {
        nouvelleManche();
    }

    /**
     * @return true si toutes les manches ont été jouées, false sinon
     */
    public boolean partieFinie() {
        return nombreMancheTotal == manches.size();
    }

    /**
     * lance une nouvelle manche et fait les actions
     * suivantes : sauvegarde la manche actuelle et génère
     * une nouvelle grille, réinitialise la liste de mots
     * trouvés, lance un nouveau minuteur et incrémente la
     * valeur des manches effectuées
     */
    public void nouvelleManche() {
        manches.add(new Manche(this.tailleVerticale, this.tailleHorizontale, this.dureeManche, this.langue));
    }

    /**
     * @return les joueurs qui ont le plus de points
     */
    public List<Joueur> getJoueurGagnant() {
        ArrayList<Joueur> joueursGagnants = new ArrayList<>();
        HashMap<Joueur, Integer> pointsParJoueur = this.getPoints();
        int max = 0;
        for (Joueur joueur : pointsParJoueur.keySet()) {
            if (pointsParJoueur.get(joueur) == max) {
                joueursGagnants.add(joueur);
            } else if (pointsParJoueur.get(joueur) > max) {
                max = pointsParJoueur.get(joueur);
                joueursGagnants.clear();
                joueursGagnants.add(joueur);
            }
        }
        return joueursGagnants;
    }

    public void ajouterJoueur(Joueur joueur) {
        joueurs.add(joueur);
    }

    public void enleverJoueur(Joueur joueur) {
        joueurs.remove(joueur);
    }

    public HashSet<Joueur> getJoueurs() {
        return joueurs;
    }

    /**
     * @return la manche courante
     */
    private Manche getMancheCourante() {
        return manches.get(manches.size() - 1);
    }

    /**
     * @param lettre les lettres trouvés
     * @param joueur joueur qui a trouvé les lettres
     */
    public void ajouterMotTrouve(LinkedList<Lettre> lettre, Joueur joueur) {
        getMancheCourante().ajouterMotTrouve(lettre, joueur);
    }

    public HashMap<Joueur, Integer> getPoints() {
        HashMap<Joueur, Integer> pointsParJoueur = new HashMap<>();
        for (Joueur joueur : joueurs) {
            pointsParJoueur.put(joueur, 0);
        }
        for (var manche : manches) {
            var points = manche.getPoints();
            for (var joueur : points.keySet()) {
                pointsParJoueur.put(joueur, pointsParJoueur.getOrDefault(joueur, 0) + points.get(joueur));
            }
        }
        return pointsParJoueur;
    }
}
