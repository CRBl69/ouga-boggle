package com.boggle.client;

import javax.swing.*;

import com.boggle.serveur.jeu.ConfigurationServeur;
import com.boggle.serveur.jeu.Langue;
import com.boggle.serveur.jeu.Serveur;
/**
 * Affichage d'une fenêtre où se passe la sélection
 * des paramètres et initialisation de la configuration.
 */
public class AffichageConfigurationServeur extends JFrame{

    private JPanel config = new JPanel();

    public AffichageConfigurationServeur(){

        setTitle("OuGa-BoGgLe - Configuration de la partie");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(config);

        JLabel p = new JLabel("port :");
        JTextField casePort = new JTextField("8080");
        casePort.setColumns(5);
        JLabel j = new JLabel("nombre de joueurs :");
        JTextField caseNbJoueurs = new JTextField("1");
        JLabel m = new JLabel("nombre de manches :");
        JTextField caseNbManches = new JTextField("1");
        JLabel t = new JLabel("durée du timer (en secondes) :");
        JTextField caseTimer = new JTextField("0");
        JLabel h = new JLabel("taille horizontale de la grille :");
        JTextField caseGrilleH = new JTextField("4");
        JLabel v = new JLabel("taille verticale de la grille :");
        JTextField caseGrilleV = new JTextField("4");
        JLabel mp = new JLabel("mot de passe :");
        JTextField caseMdp = new JTextField();
        caseMdp.setColumns(10);

        JLabel l = new JLabel("langue :");
        JComboBox<String> lan = new JComboBox<String>();
        lan.addItem("français");
        lan.addItem("anglais");
        lan.addItem("allemand");
        lan.addItem("espagnol");

        config.add(p);
        config.add(casePort);
        config.add(j);
        config.add(caseNbJoueurs);
        config.add(m);
        config.add(caseNbManches);
        config.add(t);
        config.add(caseTimer);
        config.add(h);
        config.add(caseGrilleH);
        config.add(v);
        config.add(caseGrilleV);
        config.add(mp);
        config.add(caseMdp);
        config.add(l);
        config.add(lan);
        

        JButton go = new JButton("Lancer la partie");
        config.add(go);      

        go.addActionListener(e -> {
            var langue = switch (lan.getSelectedItem().toString()){
                case "français" ->              Langue.FR;
                case "anglais" -> Langue.EN;
                case "allemand" -> Langue.DE;
                default -> Langue.ES;
            };

            try {
                ConfigurationServeur c = new ConfigurationServeur(
                    Integer.parseInt(casePort.getText()),
                    Integer.parseInt(caseNbJoueurs.getText()),
                    Integer.parseInt(caseNbManches.getText()),
                    Integer.parseInt(caseTimer.getText()),
                    Integer.parseInt(caseGrilleH.getText()),
                    Integer.parseInt(caseGrilleV.getText()),
                    langue,
                    caseMdp.getText()
                );
                new Serveur(c);
            } catch (Exception ex) {
                System.out.println("invalide");
            }
        });
    }
}
