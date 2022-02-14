package com.boggle.client;

import javax.swing.*;
import java.awt.*;

import com.boggle.serveur.jeu.ConfigurationServeur;
import com.boggle.serveur.jeu.Langue;
import com.boggle.serveur.jeu.Serveur;
/**
 * Affichage d'une fenêtre où se passe la sélection
 * des paramètres et initialisation de la configuration.
 */
public class AffichageConfigurationServeur extends JFrame{

    public AffichageConfigurationServeur(){

        setTitle("OuGa-BoGgLe - Configuration de la partie");
        setLayout(new BorderLayout());
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String[][] labels = {
            { "Port: ", "8080" },
            { "Nombre de joueurs: ", "10" },
            { "Nombre de manches: ", "3" },
            { "Durée du timer en secondes: ", "60" },
            { "Largeur de la grille: ", "4" },
            { "Hauteur de la grille: ", "4" },
            { "Mot de passe: ", "" },
        };
        int paires = labels.length;

        JPanel config = new JPanel(new GridLayout(8,1));
        for (int i = 0; i < paires; i++) {
            JPanel groupe = new JPanel();
            JLabel label = new JLabel(labels[i][0], JLabel.TRAILING);
            JTextField textField = new JTextField(labels[i][1], 5);
            label.setLabelFor(textField);
            groupe.add(label);
            groupe.add(textField);
            config.add(groupe);
        }

        JPanel langueGroupe = new JPanel();
        JLabel langueLabel = new JLabel("Langue :");
        JComboBox<String> langueComboBox = new JComboBox<String>();
        langueLabel.setLabelFor(langueComboBox);
        langueComboBox.addItem("français");
        langueComboBox.addItem("anglais");
        langueComboBox.addItem("allemand");
        langueComboBox.addItem("espagnol");
        langueGroupe.add(langueLabel);
        langueGroupe.add(langueComboBox);

        config.add(langueGroupe);
        add(config);

        JButton go = new JButton("Lancer serveur");
        add(go, BorderLayout.SOUTH);

        go.addActionListener(e -> {
            var langue = switch (langueComboBox.getSelectedItem().toString()){
                case "français" -> Langue.FR;
                case "anglais" -> Langue.EN;
                case "allemand" -> Langue.DE;
                default -> Langue.ES;
            };

            try {
                ConfigurationServeur c = new ConfigurationServeur(
                    Integer.parseInt(((JTextField)((JPanel)config.getComponent(0)).getComponent(1)).getText()),
                    Integer.parseInt(((JTextField)((JPanel)config.getComponent(1)).getComponent(1)).getText()),
                    Integer.parseInt(((JTextField)((JPanel)config.getComponent(2)).getComponent(1)).getText()),
                    Integer.parseInt(((JTextField)((JPanel)config.getComponent(3)).getComponent(1)).getText()),
                    Integer.parseInt(((JTextField)((JPanel)config.getComponent(4)).getComponent(1)).getText()),
                    Integer.parseInt(((JTextField)((JPanel)config.getComponent(5)).getComponent(1)).getText()),
                    langue,
                    ((JTextField)((JPanel)config.getComponent(6)).getComponent(1)).getText()
                );
                new Serveur(c);
            } catch (Exception ex) {
                System.out.println("invalide");
            }
        });
    }

    public static void main(String args[]){
        AffichageConfigurationServeur acs = new AffichageConfigurationServeur();
        acs.setVisible(true);
    }
}
