package com.boggle.serveur.messages;

public class NouveauMotClavier {
    private String mot;
    private String id;
    private String auteur;

    public NouveauMotClavier(String mot, String id) {
        this.mot = mot;
        this.id = id;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getId() {
        return id;
    }

    public String getMot() {
        return mot;
    }

    public String getAuteur() {
        return auteur;
    }
}
