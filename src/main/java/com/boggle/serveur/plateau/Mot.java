package com.boggle.serveur.plateau;

import com.boggle.serveur.dictionnaire.Dictionnaire;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Mot {
    private LinkedList<Lettre> lettres;
    private final long dateCreation;
    private static AtomicInteger cpt = new AtomicInteger(0);
    private final int id;

    /**
     * Constructeur.
     *
     * @param lettres liste de lettres qui composent un mot.
     */
    public Mot(LinkedList<Lettre> lettres) {
        this.lettres = lettres;
        this.dateCreation = Calendar.getInstance().getTimeInMillis();
        id = cpt.getAndIncrement();
        if (!estMotValide()) throw new IllegalArgumentException("le mot n'est pas valide");
    }

    // TODO: à finir quand le dictionnaire sera disponible
    /**
     * Vérifie si la liste de lettres est considéré comme un mot valide selon les règles de Boogle et le dictionnaire de la langue choisie
     *
     * @param lettres liste de lettres qui composent un mot.
     * @return boolean true si le mot est valide
     */
    private boolean estMotValide() {
        return Dictionnaire.estUnMot(toString()) && lettres.size() < 3;
    }

    public String toString() {
        String res = "";
        for (int i = 0; i < lettres.size(); i++) {
            res += lettres.get(i).lettre;
        }
        return res;
    }

    public LinkedList<Lettre> getLettres() {
        return lettres;
    }

    public long getTemps() {
        return dateCreation;
    }

    public int getId() {
        return id;
    }
}
