package com.boggle.serveur.jeu;

public abstract class Sauvegarde {
    protected String chemin;

    public abstract void sauvegarder();

    public abstract void charger();
}
