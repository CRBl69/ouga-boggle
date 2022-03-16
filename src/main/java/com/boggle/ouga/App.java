package com.boggle.ouga;

import com.boggle.client.AffichageConfigurationClient;
import com.boggle.client.AffichageConfigurationServeur;
import com.boggle.util.Util;
import java.awt.*;
import javax.swing.UIManager;

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
            var a = new AffichageConfigurationServeur();
            a.setVisible(true);
        } else if (args[0].equals("client")) {
            var a = new AffichageConfigurationClient();
            a.setVisible(true);
        } else {
            System.out.println("Vous devez démarer un serveur ou un client");
            System.exit(1);
        }
    }
}
