package com.boggle.serveur.jeu;

public class Partie {
    private Manche manche;
    private long secondesRestantes;
    
    public Partie(Manche manche) {
        this.manche = manche;
        secondesRestantes = manche.getMinuteur().tempsRestant();
    }

    public Manche getManche() {
        return manche;
    }

    public long getSecondesRestantes() {
        return secondesRestantes;
    }
}
