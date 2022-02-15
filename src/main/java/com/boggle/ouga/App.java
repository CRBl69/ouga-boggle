package com.boggle.ouga;

import com.boggle.client.AffichageConfigurationClient;
import com.boggle.client.AffichageConfigurationServeur;

/** lancement du jeu */
public class App {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Vous devez démarer un serveur ou un client");
            System.exit(1);
        }
        if(args[0].equals("serveur")) {
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
