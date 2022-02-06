package com.boggle.serveur.plateau;

import java.util.LinkedList;

public class Mot {
    private LinkedList<Lettre> lettres;

    /**
     * Constructeur.
     * 
     * @param lettres liste de lettres qui composent un mot.
     */
    public Mot (LinkedList<Lettre> lettres) {
        if(estMotValide(lettres))
            this.lettres = lettres;
        else 
            throw new IllegalArgumentException();
    }

    // TODO: à finir quand le dictionnaire sera disponible
    /**
     * Vérifie si la liste de lettres est considéré comme un mot valide selon les règles de Boogle et le dictionnaire de la langue choisie
     * 
     * @param lettres liste de lettres qui composent un mot.
     * @return boolean true si le mot est valide
     */
    private static boolean estMotValide(LinkedList<Lettre> lettres) {
        return true;
    }

    public String toString() {
        String res = "";
        for(int i = 0 ; i < lettres.size(); i++) {
            res += lettres.get(i);
        }
        return res;
    }

    public LinkedList<Lettre> getLettres() {
      return lettres;
    }
}