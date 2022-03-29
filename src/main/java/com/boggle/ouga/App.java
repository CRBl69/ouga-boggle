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
                .addCommand("client", argsClient)
                .addCommand("serveur", argsServeur)
                .build();
        try {
            jc.parse(args);
            if (argsMain.gui != null) {
                if (argsMain.gui.equals("client")) {
                    new AffichageConfigurationClient().setVisible(true);
                    return;
                } else if (argsMain.gui.equals("serveur")) {
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
            } else if (jc.getParsedCommand().equals("serveur")) {
                ConfigurationServeur configServeur = new ConfigurationServeur(
                        argsMain.getPort(), argsMain.getMotDePasse(), argsServeur.getJoueursMax());
                try {
                    new Serveur(configServeur);
                } catch (IOException e) {
                    logger.error("Impossible de créer un serveur.");
                }
            } else {
                afficheHelp(jc);
                System.exit(1);
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
            names = {"--joueurs-max", "-j"},
            description = "Nombre maximal de joueurs")
    private int joueursMax = 10;

    public int getJoueursMax() {
        return joueursMax;
    }
}
