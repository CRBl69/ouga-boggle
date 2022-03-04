package com.boggle.serveur.plateau;

import com.boggle.serveur.dictionnaire.Dictionnaire;
import com.boggle.serveur.jeu.Langue;
import java.util.LinkedList;

/** Plateau du jeu contenant les lettres. */
public class Grille {
    private Lettre[][] grille;
    private final GenerateurLettre langue;
    private final int colonnes;
    private final int lignes;
    private final Langue langueChoisi;

    /**
     * Constructeur.
     *
     * @param colonnes nombre de colonnes de la grille
     * @param lignes nombre de lignes de la grille
     * @param langue la langue choisie
     */
    public Grille(int colonnes, int lignes, Langue langue) {
        if (colonnes < 1 || lignes < 1) throw new IllegalArgumentException();
        this.colonnes = colonnes;
        this.lignes = lignes;
        this.langueChoisi = langue;
        grille = new Lettre[colonnes][lignes];
        this.langue = switch (langue) {
            case FR -> new GenerateurLettreFR();
            case EN -> new GenerateurLettreEN();
            case ES -> new GenerateurLettreES();
            case DE -> new GenerateurLettreDE();};
        Dictionnaire.genererArbre(langue);
        genGrille();
    }

    /**
     * Génère la grille aléatoirement.
     */
    private void genGrille() {
        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[0].length; j++) {
                grille[i][j] = new Lettre(new Coordonnee(i, j), langue.prendreLettreAleatoire());
            }
        }
    }

    public int getColonnes() {
        return colonnes;
    }

    public int getLignes() {
        return lignes;
    }

    public Langue getLangueChoisi() {
        return langueChoisi;
    }

    /**
     * cherche toutes les occurences de la première lettre du mot dans la grille et teste si elle permet de construire le reste du mot
     * @param mot mot entré par le joueur
     * @return le mot sous forme de LinkedList<Lettre> si le mot est valide, null sinon
     */
    private LinkedList<Lettre> trouverMot(String mot) {
        for (int i = 0; i < this.lignes; i++) {
            for (int j = 0; j < this.colonnes; j++) {
                if (this.grille[i][j].lettre.charAt(0) == mot.charAt(0)) {
                    var liste = new LinkedList<Lettre>();
                    liste.add(this.grille[i][j]);
                    var trouve = checkVoisin(mot.substring(1), i, j, liste);
                    if (trouve != null) return trouve;
                }
            }
        }
        return null;
    }

    /**
     * regarde chaque lettre autour de la première lettre du mot pour trouver la lettre suivante,
     * jusqu'à ce qu'on ai trouvé toutes les lettres du mot
     * @param mot mot recherché dans la grille
     * @param x coordonnées de la première lettre
     * @param y coordonnées de la première lettre
     * @param liste liste du mot a renvoyer à la fin
     * @return le mot sous forme de LinkedList<Lettre> si on a trouvé un chemin qui le représente, null sinon
     */
    private LinkedList<Lettre> checkVoisin(String mot, int x, int y, LinkedList<Lettre> liste) {
        if (mot.length() < 1) {
            return liste;
        }
        int[][] coords = {
            {x, y - 1},
            {x, y + 1},
            {x - 1, y - 1},
            {x - 1, y},
            {x - 1, y + 1},
            {x + 1, y - 1},
            {x + 1, y},
            {x + 1, y + 1},
        };
        for (var coord : coords) {
            int a = coord[0];
            int b = coord[1];
            try {
                if (!liste.contains(grille[a][b]) && this.grille[a][b].lettre.charAt(0) == mot.charAt(0)) {
                    liste.add(grille[a][b]);
                    if (checkVoisin(mot.substring(1), a, b, liste) != null) {
                        return liste;
                    }
                    liste.remove(grille[a][b]);
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * ajoute un mot entré au clavier
     * @param lettres le mot entré au clavier
     * @return le mot sous forme de LinkedList<Lettre> si le mot est valide, null sinon
     */
    public Mot ajouterMot(String lettres) {
        if (lettres.equals("")) return null;
        LinkedList<Lettre> liste = trouverMot(lettres);
        if (liste != null) {
            try {
                return new Mot(liste);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * ajoute un mot entré à la souris
     * @param lettres mot entré à la souris
     * @return le mot sous forme de LinkedList<Lettre> si le mot est valide, null sinon
     */
    public Mot ajouterMot(LinkedList<Lettre> lettres) {
        for (int i = 0; i < lettres.size() - 1; i++) {
            if (!lettres.get(i).estACoteDe(lettres.get(i + 1)) || lettres.get(i).estSur(lettres.get(i + 1))) {
                return null;
            }
        }
        try {
            return new Mot(lettres);
        } catch (Exception e) {
            return null;
        }
    }

    public Lettre[][] getGrille() {
        return grille;
    }
}
