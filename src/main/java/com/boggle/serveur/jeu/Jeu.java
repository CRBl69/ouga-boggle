package com.boggle.serveur.jeu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import com.boggle.serveur.plateau.Grille;
import com.boggle.serveur.plateau.Lettre;
import com.boggle.serveur.plateau.Mot;

/** Fonctions relatives à la partie. */
public class Jeu {
    private HashMap<Joueur, HashSet<Mot>> listeMots;
    private HashSet<Joueur> joueurs;
    private Grille grille;
    private int nombreManche;
    private int manchesEffectuees;
    private Minuteur minuteur;
    private ArrayList<Manche> manchesDeLaPartie;

    public Jeu (int nombreManche, Grille grille, Minuteur minuteur) {
        this.listeMots = new HashMap<>();
        this.joueurs = new HashSet<>();
        this.grille = grille;
        this.manchesDeLaPartie = new ArrayList<>();
        this.minuteur = minuteur;
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
        return nombreManche == manchesEffectuees;

    }

    /**
     * @return true si le timer a atteint 0 sinon false
     */
    public boolean mancheFinie() {
        return minuteur.tempsEcoule();
    }

    /**
     * lance une nouvelle manche et fait les actions 
     * suivantes : sauvegarde la manche actuelle et génère 
     * une nouvelle grille, réinitialise la liste de mots 
     * trouvés, lance un nouveau minuteur et incrémente la 
     * valeur des manches effectuées
     */
    public void nouvelleManche() {
        manchesDeLaPartie.add(new Manche(grille, listeMots, minuteur, getJoueurGagnant()));
        grille = new Grille(grille.getColonne(), grille.getLigne(), grille.getLangueChoisi());
        minuteur = new Minuteur(minuteur.getSec());
        listeMots.clear();
        manchesEffectuees++;

    }

    /**
     * Ajoute un mot valide dans la liste de mots trouvés par
     * le joueur.
     * @param mot mot qui a été trouvé
     * @param joueur joueur qui a trouvé le mot
     */
    public void ajouterMotTrouve(LinkedList<Lettre> lettre, Joueur joueur) {
        Mot mot = new Mot(lettre);
        if(!listeMots.containsKey(joueur)) {
            listeMots.put(joueur, new HashSet<>());
        }
        listeMots.get(joueur).add(mot);
    }

    /**
     * @return null s'il n'y a pas de gagnant sinon 
     * retourne le premier joueur qui a eu le plus de point
     */
    public Joueur getJoueurGagnant() {
        Joueur joueurGagnant = null;
        int max = 0;
        var points = getPoints();
        for(Joueur joueur : points.keySet()) {
            var pointsJoueur = points.get(joueur);
            if(max < pointsJoueur) {
                max = pointsJoueur;
                joueurGagnant = joueur;
            } else if(joueurGagnant != null && max == pointsJoueur ) {
                var dernierMotGagnant = getdernierMotAjouter(joueurGagnant);
                var dernierMotJoueur = getdernierMotAjouter(joueur);
                
                if(dernierMotJoueur.getId() < dernierMotGagnant.getId()) {
                    joueurGagnant = joueur;
                }
            }
        }
        return joueurGagnant;
    }

    /**
     * Parcours la liste de mots pour trouver le dernier mot que le joueur à ajouter
     * @param joueur joueur dont on souhaite connaître le dernier mot
     * @return le dernier mot ajouté dans la liste de mot
     */
    private Mot getdernierMotAjouter(Joueur joueur) {
        return listeMots.get(joueur)
                .stream()
                .max(Comparator.comparing(Mot::getId))
                .get();
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

    public HashSet<Joueur> getJoueurs() {
        return joueurs;
    }
}
