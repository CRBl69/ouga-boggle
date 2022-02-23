package com.boggle.client;

import java.awt.*;
import javax.swing.*;

public class AffichageStatus extends JFrame {
  private boolean pret = false;
  private JButton bouton;

  public AffichageStatus(Client c) {
    setSize(500, 500);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    bouton = new JButton("PRET");

    bouton.addActionListener(
        a -> {
          pret = !pret;
          bouton.setText(pret ? "PAS PRET" : "PRET");
          c.envoyerStatus(pret);
        });

    add(bouton, BorderLayout.CENTER);
  }
}
