package com.boggle.ouga;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.ParametersDelegate;
import com.boggle.client.AffichageConfigurationClient;
import com.boggle.client.AffichageConfigurationServeur;
import com.boggle.util.Util;
import java.awt.*;
import javax.swing.UIManager;
import com.boggle.serveur.jeu.Langue;

/** lancement du jeu */
public class App {

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

        if (args.length != 1) {
            System.out.println("Vous devez démarer un serveur ou un client");
            System.exit(1);
        }
        if (args[0].equals("serveur")) {
            if(args[1] != "") {
                LanceDepuisLigneDeCommande(args, false);
            } else {
                var a = new AffichageConfigurationServeur();
                a.setVisible(true);
            }
        } else if (args[0].equals("client")) {
            if(args[1] != "") {
                LanceDepuisLigneDeCommande(args, true);
            } else {
                var a = new AffichageConfigurationClient();
                a.setVisible(true);
            }
        } else {
            System.out.println("Vous devez démarer un serveur ou un client");
            System.exit(1);
        }
    }

    /**
     * Lance un serveur ou un client à partir des arguments passés dans la ligne de commande
     * @param args arguments passés dans la ligne de commande
     * @param client true si on lance un client sinon false
     */
    private static void LanceDepuisLigneDeCommande(String[] args, boolean client) {
        ArgumentsClient argClient = new ArgumentsClient();
        ArgumentsServeur argServeur = new ArgumentsServeur();
        try {
            var jc = JCommander.newBuilder()
                .addObject(client?argClient:argServeur)
                .addCommand("serveur", argServeur)
                .addCommand("client", argClient)
                .build();
            jc.parse(args);
        } catch(ParameterException e) {
            afficheHelp(client);
            System.exit(1);
        }
    }

    private static void afficheHelp(boolean client) {
        var jct = JCommander.newBuilder()
            .addObject(client? new ArgumentsClient():new ArgumentsServeur())
            .build();
        jct.usage();
        System.exit(0);
    }
}

class Port {
    @Parameter(names = "--port")
    private int port;

    public int getPort() {
        return port;
    }
}
class ArgumentsClient {
    @ParametersDelegate
    private Port port = new Port();

    @Parameter(names = "--pseudo", description = "Pseudo de l'utilisateur")
    private String pseudo;
    
    @Parameter(names = "--ip")
    private String ip;

    @Parameter(names = "--mdp", description = "Mot de passe")
    private String mdp;

    public int getPort() {
        return port.getPort();
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getIp() {
        return ip;
    }

    public String getMdp() {
        return mdp;
    }

}

class ArgumentsServeur {
    @ParametersDelegate
    private Port port = new Port();

    @Parameter(names = "--nbManche", description = "Nombre de manche de la partie")
    private String nbManche;

    @Parameter(names = "--timer", description = "Le temps du timer en seconde")
    private int timer;

    @Parameter(names = "--tailleGrilleH", description = "Dimension horizontale de la grille")
    private int tailleGrilleH;

    @Parameter(names = "--tailleGrilleV", description = "Dimension verticale de la grille")
    private int tailleGrilleV;

    @Parameter(names = "--langue", converter = Langue.class)
    private Langue langue;

    @Parameter(names = "--mdp", description = "Mot de passe")
    private String mdp;

    public int getPort() {
        return port.getPort();
    }

    public String getNbManche() {
        return nbManche;
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

    public String getMdp() {
        return mdp;
    }
}
