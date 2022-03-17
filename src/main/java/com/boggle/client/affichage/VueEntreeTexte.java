package com.boggle.client.affichage;

import java.awt.*;
import javax.swing.*;

public class VueEntreeTexte extends JTextField {
    public VueEntreeTexte(EnvoyerMotClavier envoyeur) {
        super();

        this.setPreferredSize(new Dimension(400, 100));

        addActionListener((ac) -> {
            String mot = ac.getActionCommand();

            if (mot.length() > 0) {
                envoyeur.envoyerMotClavier(mot);
            }
        });
    }

    public void activer(boolean activer) {
        this.setEnabled(activer);
        this.setText("");
    }

    public interface EnvoyerMotClavier {
        void envoyerMotClavier(String mot);
    }
}
