package com.boggle.serveur.plateau;

import com.boggle.serveur.jeu.Langue;

import java.util.LinkedList;

import com.boggle.serveur.dictionnaire.Dictionnaire;
import com.boggle.serveur.jeu.Langue;

/** Plateau du jeu contenant les lettres. */
public class Grille {
    protected Lettre[][] grille;
    private final GenerateurLettre langue;
    private final int colonne;
    private final int ligne;
    private final Langue langueChoisi;

    /**
     * Constructeur.
     *
     * @param colonne nombre de colonnes de la grille
     * @param ligne nombre de lignes de la grille
     * @param langue la langue choisie
     */
    public Grille(int colonne, int ligne, Langue langue) {
        if (colonne < 1 || ligne < 1) throw new IllegalArgumentException();
        this.colonne = colonne;
        this.ligne = ligne;
        this.langueChoisi = langue;
        grille = new Lettre[colonne][ligne];
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

    public int getColonne() {
        return colonne;
    }

    public int getLigne() {
        return ligne;
    }

    public Langue getLangueChoisi() {
        return langueChoisi;
    }

    private LinkedList<Lettre> trouverMot(String mot) {
        for(int i=0; i<this.getLigne(); i++) {
            for(int j=0; j<this.getColonne(); j++) {
                if(this.grille[i][j].lettre.charAt(0) == mot.charAt(0)) {
                    return checkVoisin(mot.substring(1), j, i, new LinkedList<Lettre>());
                }
            }
        }
        return null;
    }

    private LinkedList<Lettre> checkVoisin(String mot, int x, int y, LinkedList<Lettre> liste) {
        if(mot.length()<1 ){
            return liste;
        }

        int[][] coords = {
            { x, y-1 },
            { x, y+1 },
            { x-1, y-1 },
            { x-1, y },
            { x-1, y+1 },
            { x+1, y-1 },
            { x+1, y },
            { x+1, y+1 },
        };

        for(var coord: coords) {
            int a = coord[0];
            int b = coord[1];
            try {
                if (!liste.contains(grille[a][b]) && this.grille[a][b].lettre.charAt(0) == mot.charAt(0)){
                    liste.add(grille[a][b]);
                    if(checkVoisin(mot.substring(1), a, b, liste) != null) {
                        return liste;
                    }
                    liste.remove(grille[a][b]);
                }
            } catch(Exception e) {}
        }
        return null;
    }

    public Mot ajouterMot(String lettres) {
        LinkedList<Lettre> liste = trouverMot(lettres);
        if(liste != null) {
            try {
                return new Mot(liste);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public Mot ajouterMot(LinkedList<Lettre> lettres) {
        for(int i = 0; i < lettres.size() - 1; i++) {
            if(!lettres.get(i).estACoteDe(lettres.get(i+1)) && !lettres.get(i).estSur(lettres.get(i+1))) {
                return null;
            }
        }
        try {
            return new Mot(lettres);
        } catch (Exception e) {
            return null;
        }
    }

}
