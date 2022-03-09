package com.boggle.serveur.messages;

import com.boggle.serveur.plateau.Lettre;

public class NouveauMotSouris {
    private String id;
    private String auteur;
    private Lettre[] lettres;

    public NouveauMotSouris() {}

    public String getId() {
        return id;
    }

    public Lettre[] getLettres() {
        return lettres;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getAuteur() {
        return auteur;
    }
}
