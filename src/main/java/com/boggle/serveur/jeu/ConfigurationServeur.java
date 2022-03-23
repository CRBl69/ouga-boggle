package com.boggle.serveur.jeu;

import com.boggle.serveur.jeu.Jeu.Modes;
import com.boggle.serveur.jeu.modes.Normal;

/** Paramètre de la partie i.e nombre de joueur, le timer, etc... */
public class ConfigurationServeur {

    public final int port;
    public final int nbJoueurs;
    public final int nbManches;
    public final int timer;
    public final int tailleGrilleH;
    public final int tailleGrilleV;
    public final Langue langue;
    public final String mdp;
    public final Modes modeDeJeu;

    public ConfigurationServeur(
            int port,
            int nbJoueurs,
            int nbManches,
            int timer,
            int tailleGrilleH,
            int tailleGrilleV,
            Langue langue,
            String mdp,
            Modes modeDeJeu) {
        this.port = port;
        this.nbJoueurs = nbJoueurs;
        this.nbManches = nbManches;
        this.timer = timer;
        this.tailleGrilleH = tailleGrilleH;
        this.tailleGrilleV = tailleGrilleV;
        this.langue = langue;
        this.mdp = mdp;
        this.modeDeJeu = modeDeJeu;
    }
}
