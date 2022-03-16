package com.boggle.serveur.jeu;

import java.io.Serializable;
import java.util.Calendar;

public class Minuteur implements Serializable {
    private Calendar tempsFin;
    private int sec;
    private Jeu jeu;

    /**
     * Constructeur.
     *
     * @param sec temps en secondes.
     */
    public Minuteur(int sec, Jeu jeu) {
        this.sec = sec;
        tempsFin = Calendar.getInstance();
        tempsFin.set(Calendar.SECOND, tempsFin.get(Calendar.SECOND) + sec);
        this.jeu = jeu;
        relancerManche();
    }

    /**
     * Donne le temps restant.
     *
     * @return long retourne le temps en secondes.
     */
    public long tempsRestant() {
        long diff = (tempsFin.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000;
        return diff;
    }

    /**
     * Vérifie si le temps est écoulé.
     *
     * @return boolean true si le temps est écoulé.
     */
    public boolean tempsEcoule() {
        if (tempsFin.getTimeInMillis() - Calendar.getInstance().getTimeInMillis() < 0) return true;
        return false;
    }

    public int getSec() {
        return sec;
    }

    private void relancerManche() {
        Thread t = new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000 * tempsRestant());
                    jeu.finirManche();
                    Thread.sleep(1000 * 10);
                    jeu.nouvelleManche();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void mettreEnPause() {
        sec = (int) tempsRestant();
    }

    public void reprendre() {
        tempsFin = Calendar.getInstance();
        tempsFin.set(Calendar.SECOND, tempsFin.get(Calendar.SECOND) + sec);
        relancerManche();
    }
}
