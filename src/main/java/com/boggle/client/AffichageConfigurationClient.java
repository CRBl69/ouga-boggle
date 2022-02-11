package com.boggle.client;

import javax.swing.*;

import com.boggle.serveur.jeu.ConfigurationClient;

public class AffichageConfigurationClient extends JFrame {

    private JPanel config = new JPanel();

    public AffichageConfigurationClient(){

        setTitle("OuGa-BoGgLe - Configuration de la partie");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(config);

        JLabel ip = new JLabel("IP :");
        JTextField caseIP = new JTextField("127.0.0.1");
        caseIP.setColumns(20);
        JLabel p = new JLabel("port :");
        JTextField casePort = new JTextField("8080");
        casePort.setColumns(5);
        JLabel j = new JLabel("pseudo :");
        JTextField casePseudo = new JTextField();
        casePseudo.setColumns(10);
        JLabel mp = new JLabel("mot de passe :");
        JTextField caseMdp = new JTextField();
        caseMdp.setColumns(10);

        config.add(ip);
        config.add(caseIP);
        config.add(p);
        config.add(casePort);
        config.add(j);
        config.add(casePseudo);
        config.add(mp);
        config.add(caseMdp);

        JButton go = new JButton("Lancer la partie");
        config.add(go);      

        go.addActionListener(e -> {

            try {
                ConfigurationClient c = new ConfigurationClient(
                    caseIP.getText(),
                    Integer.parseInt(casePort.getText()),
                    casePseudo.getText(),
                    caseMdp.getText()
                );
                new Client(c);
            } catch (Exception ex) {
                System.out.println("invalide");
            }
        });
    }
}
