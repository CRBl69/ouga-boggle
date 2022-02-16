package com.boggle.serveur.jeu;

import java.util.Calendar;

public class Minuteur {
    private final Calendar tempsFin;
    private final int sec;

    /**
     * Constructeur.
     * 
     * @param sec temps en secondes.
     */
    public Minuteur(int sec) {
        this.sec = sec;
        tempsFin = Calendar.getInstance();
        tempsFin.set(Calendar.SECOND, tempsFin.get(Calendar.SECOND)+sec);
    }

    /**
     * Donne le temps restant.
     * 
     * @return long retourne le temps en secondes.
     */
    public long tempsRestant() {
        long diff = (tempsFin.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()) / 1000;
        return diff; 
    }

    /**
     * Vérifie si le temps est écoulé.
     * 
     * @return boolean true si le temps est écoulé. 
     */
    public boolean tempsEcoule() {
        if(tempsFin.getTimeInMillis()-Calendar.getInstance().getTimeInMillis() < 0) 
            return true;
        return false;
    }

    public int getSec() {
        return sec;
    }
}
