package com.boggle.client.affichage;

import java.awt.*;
import javax.swing.*;

public class VueInfos extends JPanel {
    private JLabel manchesLabel = new JLabel("Manches : 0");
    private JLabel pointsLabel = new JLabel("Points : 0");
    private JLabel statusLabel = new JLabel("Status : pause");

    private int manchesTotal;
    private int manchesJouees;
    private int points = 0;

    public VueInfos(int manchesTotal, int manchesJouees) {
        this.setLayout(new GridLayout(3, 1));
        this.setBorder(BorderFactory.createTitledBorder("Infos"));
        this.add(this.manchesLabel);
        this.add(this.pointsLabel);
        this.add(this.statusLabel);

        this.manchesTotal = manchesTotal;
        this.manchesJouees = manchesJouees;
    }

    public VueInfos(int manchesTotal) {
        this(manchesTotal, 0);
    }

    public void ajouterPoints(int points) {
        this.points += points;
        this.pointsLabel.setText("Points : " + Integer.toString(this.points));
    }

    public void updateStatus(boolean mancheEnCours) {
        if (mancheEnCours) {
            this.statusLabel.setText("Status : manche en cours");
        } else {
            this.statusLabel.setText("Status : pause");
        }
    }

    public void updateManches() {
        this.manchesJouees++;
        this.manchesLabel.setText(String.format("Manches : %d/%d", this.manchesJouees, this.manchesTotal));
    }
}
