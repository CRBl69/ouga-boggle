package com.boggle.serveur.jeu.modes;

import com.boggle.serveur.ServeurInterface;
import com.boggle.serveur.jeu.Jeu;
import com.boggle.serveur.jeu.Langue;
import com.boggle.serveur.jeu.Manche;

public class Normal extends Jeu {

    public Normal(
            int nombreManche,
            int dureeManche,
            int tailleVerticale,
            int tailleHorizontale,
            Langue langue,
            ServeurInterface serveur) {
        super(nombreManche, dureeManche, tailleVerticale, tailleHorizontale, langue, serveur);
    }

    public void nouvelleManche() {
        demarrerManche(new Manche(this.tailleVerticale, this.tailleHorizontale, this.dureeManche, this.langue));
        if (dureeManche != 0) {
            Thread t = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(dureeManche * 1000);
                        finirManche();
                        if (!estFini()) {
                            Thread.sleep(10000);
                            nouvelleManche();
                        } else {
                            finirJeu();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            t.start();
        } else {
            // TODO: implémenter une manche sans minuteur (par vote ?)
        }
    }
}
