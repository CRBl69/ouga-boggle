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

    public void updateStatus(Status status, boolean desactive) {
        switch (status) {
            case MANCHE_EN_COURS:
                if (desactive) {
                    this.statusLabel.setText("Status : éliminé");
                } else {
                    this.statusLabel.setText("Status : manche en cours");
                }
                break;
            case PAUSE:
                this.statusLabel.setText("Status : pause");
                break;
            case FIN:
                this.statusLabel.setText("Status : fin");
                break;
        }
    }

    public void updateManches() {
        this.manchesJouees++;
        this.manchesLabel.setText(String.format(
                "Manches : %d/%s",
                this.manchesJouees, this.manchesTotal != 0 ? Integer.toString(this.manchesTotal) : "?"));
    }

    public enum Status {
        MANCHE_EN_COURS,
        PAUSE,
        FIN,
    }
}
