package com.boggle.serveur.jeu;

import com.boggle.serveur.plateau.Grille;
import com.boggle.serveur.plateau.Lettre;
import com.boggle.serveur.plateau.Mot;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/** utiliser pour la sauvegarde des manches */
public class Manche {
    private Grille grille;
    private HashMap<Joueur, HashSet<Mot>> listeMots;
    private Minuteur minuteur;

    public Manche(int tailleVerticale, int tailleHorizontale, int dureeTimer, Langue langue) {
        this.grille = new Grille(tailleVerticale, tailleHorizontale, langue);
        this.listeMots = new HashMap<Joueur, HashSet<Mot>>();
        this.minuteur = new Minuteur(dureeTimer);
    }

    public Grille getGrille() {
        return grille;
    }

    public HashMap<Joueur, HashSet<Mot>> getListeMots() {
        return listeMots;
    }

    public Minuteur getMinuteur() {
        return minuteur;
    }

    /**
     * Retourne les points de tous les joueurs.
     * @return une map des points de tous les joueurs
     */
    public Map<Joueur, Integer> getPoints() {
        Map<Joueur, Integer> points = new HashMap<>();
        for (Joueur joueur : listeMots.keySet()) {
            int acc = 0;
            for (Mot mot : listeMots.get(joueur)) {
                switch (mot.getLettres().size()) {
                    case 3:
                    case 4:
                        acc += 1;
                        break;
                    case 5:
                        acc += 2;
                        break;
                    case 6:
                        acc += 3;
                        break;
                    case 7:
                        acc += 5;
                        break;
                    default:
                        acc += 11;
                        break;
                }
            }
            points.put(joueur, acc);
        }
        return points;
    }

    /**
     * @return true si le timer a atteint 0 sinon false
     */
    public boolean mancheFinie() {
        return minuteur.tempsEcoule();
    }

    /**
     * Ajoute un mot valide dans la liste de mots trouvés par
     * le joueur.
     * @param lettre mot qui a été trouvé
     * @param joueur joueur qui a trouvé le mot
     */
    public void ajouterMotTrouve(LinkedList<Lettre> lettre, Joueur joueur) {
        Mot mot = grille.ajouterMot(lettre);
        if (!listeMots.containsKey(joueur)) {
            listeMots.put(joueur, new HashSet<>());
        }
        listeMots.get(joueur).add(mot);
    }

    /**
     * Ajoute un mot valide dans la liste de mots trouvés par
     * le joueur.
     * @param lettre mot qui a été trouvé
     * @param joueur joueur qui a trouvé le mot
     */
    public void ajouterMotTrouve(String lettre, Joueur joueur) {
        Mot mot = grille.ajouterMot(lettre);
        if(!listeMots.containsKey(joueur)) {
            listeMots.put(joueur, new HashSet<>());
        }
        listeMots.get(joueur).add(mot);
    }
}
