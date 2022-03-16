package com.boggle.ouga;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.boggle.client.AffichageConfigurationClient;
import com.boggle.client.AffichageConfigurationServeur;
import com.boggle.client.Client;
import com.boggle.serveur.Serveur;
import com.boggle.serveur.jeu.ConfigurationClient;
import com.boggle.serveur.jeu.ConfigurationServeur;
import com.boggle.serveur.jeu.Langue;
import com.boggle.util.ConnexionServeurException;
import com.boggle.util.Logger;
import com.boggle.util.Util;
import java.awt.*;
import java.io.IOException;
import javax.swing.UIManager;

/** lancement du jeu */
public class App {
    private static Logger logger = Logger.getLogger("APP");

    public static void main(String[] args) {
        // Active le anti-aliasing pour que le texte ne soit pas pixelisé
        System.setProperty("awt.useSystemAAFontSettings", "on");

        // Utilise le font Noto Sans
        Util.setUIFont(new javax.swing.plaf.FontUIResource("Noto Sans", Font.PLAIN, 14));

        // Utilise le style de l'OS
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (Exception e) {
        }

        lanceDepuisLigneDeCommande(args);
    }

    /**
     * Lance un serveur ou un client à partir des arguments passés dans la ligne de commande
     * @param args arguments passés dans la ligne de commande
     * @param client true si on lance un client sinon false
     */
    private static void lanceDepuisLigneDeCommande(String[] args) {
        ArgumentsMain argsMain = new ArgumentsMain();
        ArgumentsClient argsClient = new ArgumentsClient();
        ArgumentsServeur argsServeur = new ArgumentsServeur();
        var jc = JCommander.newBuilder()
                .addObject(argsMain)
                .addCommand("serveur", argsServeur)
                .addCommand("client", argsClient)
                .build();
        try {
            jc.parse(args);
            if (argsMain.gui != null) {
                if (argsMain.gui.equals("client")) {
                    new AffichageConfigurationClient().setVisible(true);
                    return;
                } else if (argsMain.gui.equals("client")) {
                    new AffichageConfigurationServeur().setVisible(true);
                    return;
                }
            }
            if (jc.getParsedCommand().equals("client")) {
                ConfigurationClient configClient = new ConfigurationClient(
                        argsClient.getHost(), argsMain.getPort(), argsClient.getPseudo(), argsMain.getMotDePasse());
                try {
                    new Client(configClient);
                } catch (ConnexionServeurException e) {
                    logger.error("Impossible de se connecter au serveur.");
                }
            } else {
                ConfigurationServeur configServeur = new ConfigurationServeur(
                        argsMain.getPort(),
                        argsServeur.getNbJoueursMax(),
                        argsServeur.getNbManches(),
                        argsServeur.getTimer(),
                        argsServeur.getTailleGrilleH(),
                        argsServeur.getTailleGrilleV(),
                        argsServeur.getLangue(),
                        argsMain.getMotDePasse());
                try {
                    new Serveur(configServeur);
                } catch (IOException e) {
                    logger.error("Impossible de créer un serveur.");
                }
            }
        } catch (ParameterException e) {
            afficheHelp(jc);
            System.exit(1);
        }
    }

    private static void afficheHelp(JCommander jct) {
        jct.usage();
        System.exit(0);
    }
}

class ArgumentsClient {
    @Parameter(
            names = {"--pseudo", "-P"},
            description = "Pseudo de l'utilisateur",
            required = true)
    private String pseudo;

    @Parameter(names = {"--host", "-h"})
    private String host = "127.0.0.1";

    public String getPseudo() {
        return pseudo;
    }

    public String getHost() {
        return host;
    }
}

class ArgumentsServeur {
    @Parameter(
            names = {"--nombre-manche", "-n"},
            description = "Nombre de manche de la partie")
    private int nbManche = 3;

    @Parameter(
            names = {"--minuteur", "-t"},
            description = "Le temps du minuteur en secondes")
    private int timer = 60;

    @Parameter(
            names = {"--taille-grille-horizontale", "-h"},
            description = "Dimension horizontale de la grille")
    private int tailleGrilleH = 4;

    @Parameter(
            names = {"--taille-grille-verticale", "-v"},
            description = "Dimension verticale de la grille")
    private int tailleGrilleV = 4;

    @Parameter(
            names = {"--langue", "-l"},
            converter = Langue.class)
    private Langue langue = Langue.FR;

    @Parameter(
            names = {"--joueurs-max", "-j"},
            description = "Nombre maximal de joueurs")
    private int nbJoueursMax = 10;

    public int getNbManches() {
        return nbManche;
    }

    public int getNbJoueursMax() {
        return nbJoueursMax;
    }

    public int getTimer() {
        return timer;
    }

    public int getTailleGrilleH() {
        return tailleGrilleH;
    }

    public int getTailleGrilleV() {
        return tailleGrilleV;
    }

    public Langue getLangue() {
        return langue;
    }
}
