package com.boggle.serveur.messages;

import com.boggle.serveur.jeu.Jeu;
import com.boggle.serveur.plateau.Coordonnee;

public class FinManche {
    private Joueur[] joueurs;

    static class Joueur {
        private String nom;
        private int points;
        private Mot[] mots;

        public String getNom() {
            return nom;
        }

        public int getPoints() {
            return points;
        }

        public Mot[] getMots() {
            return mots;
        }
    }

    static class Mot {
        private String mot;
        private int points;
        private Coordonnee[] coordonnees;

        public String getMot() {
            return mot;
        }

        public int getPoints() {
            return points;
        }

        public Coordonnee[] getCoordonnees() {
            return coordonnees;
        }
    }

    public FinManche(Jeu jeu) {
        this.joueurs = new Joueur[jeu.getJoueurs().size()];
        int index = 0;
        var points = jeu.getPoints();
        var mots = jeu.getListeMots();
        for (var joueur : jeu.getJoueurs()) {
            Joueur j = new Joueur();
            j.nom = joueur.nom;
            j.points = points.get(joueur);
            var liste = mots.get(joueur);
            j.mots = new Mot[liste.size()];
            if (liste != null) {
                int indexMots = 0;
                for (var m : liste) {
                    Mot mot = new Mot();
                    mot.mot = m.toString();
                    mot.points = m.getPoints();
                    mot.coordonnees = m.getLettres().stream().map(l -> l.coord).toArray(Coordonnee[]::new);
                    j.mots[indexMots++] = mot;
                }
            }
            this.joueurs[index++] = j;
        }
    }

    public Joueur[] getJoueurs() {
        return joueurs;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fin manche :\n");
        for (var joueur : joueurs) {
            sb.append(joueur.nom).append(" : ").append(joueur.points).append(" points\n");
            for (var mot : joueur.mots) {
                sb.append("\t").append(mot.mot).append(" : ").append(mot.points).append(" points\n");
                for (var coord : mot.coordonnees) {
                    sb.append("\t\t").append(coord).append("\n");
                }
            }
        }
        return sb.toString();
    }
}
