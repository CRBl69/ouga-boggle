package com.boggle.client.affichage;

import com.boggle.client.AffichageJeu;
import java.awt.*;
import javax.swing.*;

public class VueEntreeTexte extends JTextField {
    public VueEntreeTexte(AffichageJeu affichageJeu) {
        super();

        this.setPreferredSize(new Dimension(400, 100));

        addActionListener((ac) -> {
            String mot = ac.getActionCommand();

            if (mot.charAt(0) == '/') {
                switch (mot.substring(1)) {
                    case "lobby":
                        affichageJeu.lobby();
                        break;
                }
            } else if (mot.length() > 0) {
                affichageJeu.envoyerMotClavier(mot);
            }
        });
    }

    public void activer(boolean activer) {
        this.setEnabled(activer);
        this.setText("");
    }
}
