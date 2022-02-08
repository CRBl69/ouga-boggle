package com.boggle.serveur.jeu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.boggle.serveur.plateau.Grille;
import com.boggle.serveur.plateau.Mot;

/** Fonctions relatives à la partie. */
public class Jeu {
    private HashMap<Joueur, ArrayList<Mot>> listeMots;
    private ArrayList<Joueur> joueurs;
    private Grille grille;
    private int nombreManche;
    private int mancheEffectuees;

    public Jeu (int nombreManche, Grille grille) {
        this.listeMots = new HashMap<>();
        this.joueurs = new ArrayList<>();
        this.grille = grille;
        if(nombreManche < 1) {
            throw new IllegalArgumentException("La nombre de manches doit être supérieur ou égal à 1.");
        } else {
            this.nombreManche = nombreManche;
        }
    }

    /**
     * @return true si toutes les manches ont été jouées, false sinon
     */
    public boolean partieFinie() {
        return nombreManche == mancheEffectuees;

    }

    /**
     * @return true si le timer a atteint 0 sinon false
     */
    public boolean mancheFinie() {
        // TODO: implémenter la condition d'arret de la manche
        // true quand le timer a atteint 0
        // sinon false
        return true;
    }

    /**
     * Ajoute un mot valide dans la liste de mot trouvés par
     * le joueur.
     * @param mot mot qui a été trouvé
     * @param joueur joueur qui a trouvé le mot
     */
    public void ajouterMotTrouve(Mot mot, Joueur joueur) {
        if(!listeMots.containsKey(joueur)) {
            listeMots.put(joueur, new ArrayList<>());
        }
        listeMots.get(joueur).add(mot);
    }

    /**
     * @return le(s) joueur(s) qui a/ont le plus de points
     */
    public ArrayList<Joueur> getJoueursGagnants() {
        ArrayList<Joueur> gagnant = new ArrayList<>();
        int max = 0;
        var points = getPoints();
        for(Joueur joueur : points.keySet()) {
            var pointsJoueur = points.get(joueur);
            if(max < pointsJoueur) {
                max = pointsJoueur;
                gagnant.clear();
                gagnant.add(joueur);
            } else if(max == pointsJoueur) {
                max = pointsJoueur;
                gagnant.add(joueur);
            }
        }
        return gagnant;
    }

    /**
     * Retourne les points de tous les joueurs.
     * @return une map des points de tous les joueurs
     */
    public Map<Joueur, Integer> getPoints() {
        Map<Joueur, Integer> points = new HashMap<>();
        for(Joueur joueur : listeMots.keySet()) {
            int acc = 0;
            for(Mot mot : listeMots.get(joueur)) {
                switch(mot.getLettres().size()){
                    case 3:
                    case 4: acc += 1; break;
                    case 5: acc += 2; break;
                    case 6: acc += 3; break;
                    case 7: acc += 5; break;
                    default: acc += 11; break;
                }
            }
            points.put(joueur, acc);
        }
        return points;
    }

    public void ajouterJoueur(Joueur joueur) {
        joueurs.add(joueur);
    }

    public void enleverJoueur(Joueur joueur) {
        joueurs.remove(joueur);
    }

    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }
}
