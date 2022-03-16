package com.boggle.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class AffichageStatus extends JFrame {
    private boolean pret = false;
    private JButton bouton;
    private JLabel nPrets = new JLabel();
    private JLabel nJoueurs = new JLabel();
    private JTextPane joueurs = new JTextPane();
    private Client client;

    public AffichageStatus(Client c) {
        client = c;

        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        bouton = new JButton("PRET");

        bouton.addActionListener(a -> {
            pret = !pret;
            bouton.setText(pret ? "PAS PRET" : "PRET");
            c.envoyerStatus(pret);
        });

        JPanel infoPanel = new JPanel(new FlowLayout());
        infoPanel.add(nJoueurs);
        infoPanel.add(nPrets);

        JScrollPane scroll = new JScrollPane(this.joueurs);
        scroll = new JScrollPane(this.joueurs);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBorder(null);

        add(infoPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bouton, BorderLayout.SOUTH);
        update();
    }

    public void update() {
        nPrets.setText(String.format(
                "Prets: %d", client.getJoueurs().stream().filter(c -> c.estPret).count()));
        nJoueurs.setText(String.format("Connectes: %d", client.getJoueurs().size()));
        joueurs.setText("");
        client.getJoueurs().forEach(j -> {
            appendToPane(joueurs, j.nom + "\n", j.estPret ? Color.GREEN : Color.RED);
        });
    }

    private void appendToPane(JTextPane tp, String msg, Color c) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
}
