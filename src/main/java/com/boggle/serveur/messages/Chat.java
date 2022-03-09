package com.boggle.serveur.messages;

public class Chat {
    private String message;
    private String auteur;

    public Chat() {}

    public String getMessage() {
        return message;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String toString() {
        return String.format("%s : %s", auteur, message);
    }
}
