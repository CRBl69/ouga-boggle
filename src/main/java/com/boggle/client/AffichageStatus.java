package com.boggle.client;

import java.awt.*;
import javax.swing.*;

public class AffichageStatus extends JFrame {
    private boolean pret = false;
    private JButton bouton;
    private JLabel nPrets = new JLabel();
    private JLabel nJoueurs = new JLabel();
    private Client client;
    
    public AffichageStatus(Client c) {
        client = c;

        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        bouton = new JButton("PRET");
	
        bouton.addActionListener(
                                 a -> {
                                     pret = !pret;
                                     bouton.setText(pret ? "PAS PRET" : "PRET");
                                     c.envoyerStatus(pret);
                                 });

	JPanel panel = new JPanel(new FlowLayout());
	panel.add(nJoueurs);
	panel.add(nPrets);
	
	add(panel, BorderLayout.NORTH);
	add(bouton, BorderLayout.CENTER);
	update();
    }

    public void update() {
	nPrets.setText(String.format("Prets: %d", client.getNPrets()));
	nJoueurs.setText(String.format("Connectes: %d", client.getNClients()));
    }
}
