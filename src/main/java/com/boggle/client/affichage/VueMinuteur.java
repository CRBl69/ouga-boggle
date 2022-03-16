package com.boggle.client.affichage;

import java.awt.*;
import javax.swing.*;

public class VueMinuteur extends JPanel {
    private JLabel label;

    public VueMinuteur() {
        super();
        this.setPreferredSize(new Dimension(200, 100));
        this.setLayout(new BorderLayout());
        var border = BorderFactory.createTitledBorder("Minuteur");
        this.setBorder(border);
        this.label = new JLabel("0");
        this.add(this.label, BorderLayout.CENTER);
    }

    public void setMinuteur(int minuteur) {
        this.label.setText(Integer.toString(minuteur));
    }

    public void start(int minuteur) {
        this.label.setText(Integer.toString(minuteur));
        MinuteurThread t = new MinuteurThread();
        t.setTempsRestant(minuteur);
        t.start();
    }

    private class MinuteurThread extends Thread {
        private int tempsRestant;

        public void setTempsRestant(int tempsRestant) {
            this.tempsRestant = tempsRestant;
        }

        public void run() {
            while (tempsRestant > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tempsRestant--;
                setMinuteur(tempsRestant);
            }
            tempsRestant = 10;
            while (tempsRestant > 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tempsRestant--;
                setMinuteur(tempsRestant);
            }
        }
    }
}
