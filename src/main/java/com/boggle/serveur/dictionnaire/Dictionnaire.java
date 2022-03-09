package com.boggle.serveur.dictionnaire;

import com.boggle.serveur.jeu.Langue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/** Classe de vérification des mots. */
public class Dictionnaire {
    private static Arbre arbre = null;
    private static Langue langue;

    /**
     * Initialise le dictionnaire. Doit impérativement
     * être appelé avant d'utiliser le dictionnaire.
     */
    public static void genererArbre(Langue langue) {
        if (arbre == null || Dictionnaire.langue != langue) {
            Dictionnaire.langue = langue;
            var mots = listeDesMots(langue);
            Dictionnaire.arbre = new Arbre(mots);
        }
    }

    /**
     * Vérifie si le mot est dans le dictionnaire.
     * @param mot le mot à vérifier
     * @throws IllegalStateException si le dictionnaire n'a pas été initialisé
     * @return vrai si le mot est dans le dictionnaire, faux sinon
     */
    public static boolean estUnMot(String mot) {
        if (Dictionnaire.arbre == null) {
            throw new IllegalStateException("Arbre non initialisé");
        }
        if (mot == "") return false;
        return Dictionnaire.arbre.estUnMot(mot);
    }

    /**
     * Retourne la liste des mots du dictionnaire.
     * @param langue la langue du dictionnaire
     * @return la liste des mots du dictionnaire
     */
    private static Set<String> listeDesMots(Langue langue) {
        HashSet<String> mots = new HashSet<>();
        try {
            String nomFichier = "/langues/"
                    + switch (langue) {
                        case FR -> "fr_sans_accents.txt";
                        case DE -> "de_sans_accents.txt";
                        case ES -> "es_sans_accents.txt";
                        case EN -> "en.txt";
                    };
            FileInputStream flux = lireFichier(nomFichier);
            BufferedReader lecteur = new BufferedReader(new InputStreamReader(flux));
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                mots.add(ligne.toLowerCase());
            }
            lecteur.close();
        } catch (FileNotFoundException e) {
            System.out.println("Fichier de langue manquant");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return mots;
    }

    /**
     * Lit un fichier qui se trouve dans les ressources.
     * @param fichier le nom du fichier à lire
     * @return le flux de lecture du fichier
     */
    private static FileInputStream lireFichier(String fichier) throws FileNotFoundException {
        try {
            return new FileInputStream(
                    new File(Dictionnaire.class.getResource(fichier).toURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class Arbre {
    private Noeud tete;

    public Arbre(Set<String> listeDesMots) {
        this.tete = new Noeud(listeDesMots);
    }

    public boolean estUnMot(String mot) {
        return this.tete.estUnSousMot(mot);
    }
}

class Noeud {
    private HashMap<String, Noeud> fils;
    private boolean estUnMot;

    public Noeud(Set<String> mots) {
        this.estUnMot = mots.contains("");
        mots.remove("");
        this.fils = new HashMap<>();
        var premieresLettres = mots.stream().map(mot -> mot.substring(0, 1)).collect(Collectors.toSet());
        for (var premiereLettre : premieresLettres) {
            var fils = new Noeud(mots.stream()
                    .filter(mot -> mot.startsWith(premiereLettre))
                    .map(mot -> mot.substring(1))
                    .collect(Collectors.toSet()));
            this.fils.put(premiereLettre, fils);
        }
    }

    public boolean estUnSousMot(String mot) {
        if (mot.isEmpty()) return estUnMot;
        var fils = this.fils.get(mot.substring(0, 1));
        if (fils == null) return false;
        return fils.estUnSousMot(mot.substring(1));
    }
}
