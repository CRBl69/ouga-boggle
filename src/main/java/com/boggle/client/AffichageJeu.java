package com.boggle.client;

import com.boggle.client.Client.Serveur;
import com.boggle.serveur.messages.FinManche;
import com.boggle.serveur.messages.MotTrouve;
import com.boggle.serveur.messages.MotVerifie;
import com.boggle.serveur.plateau.*;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

/** Vue du jeu */
public class AffichageJeu extends JFrame {
    private ChampDeTexte champDeTexte = new ChampDeTexte();
    private VueGrille grille;
    public VueMots mots = new VueMots();
    private Serveur serveur;

    Consumer<Mot> action;

    private class ChampDeTexte extends JTextField {
        public ChampDeTexte() {
            super();

            addActionListener((ac) -> {
                String mot = ac.getActionCommand();

                if (mot.length() > 0) {
                    serveur.envoyerMotClavier(mot);
                }
            });
        }
    }

    /** Vue pour la grille de lettres */
    private class VueGrille extends JPanel {
        VueCase[][] contenu;

        /** Vue pour une lettre */
        private class VueCase extends JButton {
            String lettre;

            /**
             * Constructeur.
             *
             * @param l charactere a utiliser pour cette case
             */
            public VueCase(String l) {
                super(l);
                lettre = l;

                addActionListener((e) -> champDeTexte.setText(champDeTexte.getText() + lettre));
            }
        }

        /**
         * Mets à jour la grille avec les nouvelles lettres.
         *
         * @param lettres nouvelles lettres
         */
        public void miseAJour(String[][] lettres) {
            setLayout(new GridLayout(lettres[0].length, lettres.length));
            removeAll();
            contenu = new VueCase[lettres[0].length][lettres.length];
            for (int i = 0; i < lettres.length; i++) {
                for (int j = 0; j < lettres[0].length; j++) {
                    VueCase c = new VueCase(lettres[i][j]);
                    contenu[i][j] = c;
                    add(c);
                }
            }
        }
    }

    /** Vue pour les mots trouves */
    class VueMots extends JPanel {
        private JTextArea textArea;
        private JScrollPane scrollPane;

        /** Constructeur. */
        public VueMots() {
            super();
            this.setLayout(new BorderLayout());
            this.setPreferredSize(new Dimension(400, 400));
            this.setBorder(BorderFactory.createTitledBorder("Chat"));
            this.textArea = new JTextArea();
            this.textArea.setLineWrap(true);
            this.textArea.setWrapStyleWord(true);
            this.textArea.setFont(new Font("sans", Font.PLAIN, 14));
            this.textArea.setEditable(false);
            this.scrollPane = new JScrollPane(this.textArea);
            this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            DefaultCaret caret = (DefaultCaret) this.textArea.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            this.add(this.scrollPane, BorderLayout.CENTER);
        }

        /**
         * Ajoute un mot a la liste de mots
         *
         * @param mot le mot a ajouter
         */
        public void ajouterChat(String message) {
            // On ne peut pas utiliser .contains avec des String
            this.textArea.append(message + "\n");
            updateUI();
        }
    }

    /**
     * Constructeur.
     *
     * @param lettres la grille de lettres a utiliser j
     */
    public AffichageJeu(Serveur serveur) {
        super();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.serveur = serveur;

        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        grille = new VueGrille();

        champDeTexte.addActionListener((a) -> champDeTexte.setText(""));

        panel.add(grille, gbc);
        panel.add(champDeTexte, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;

        panel.add(mots, gbc);

        pack();
        setVisible(true);
    }

    /**
     * Prépare l'interface pour une nouvelle manche.
     *
     * @param lettres nouvelles lettres
     */
    public void initManche(String[][] lettres) {
        grille.miseAJour(lettres);
        grille.updateUI();

        pack();
    }

    /**
     * Ajoute un ActionListener, active a chaque entree de mot
     *
     * @param a l'ActionListener a ajouter
     */
    public void setAction(Consumer<Mot> action) {
        this.action = action;
    }

    /**
     * Ajoute un mot a la liste des mots trouves
     *
     * @param mot le mot a ajouter
     */
    public void ajouterChat(String message) {
        mots.ajouterChat(message);
    }

    /**
     * Affiche les informations du mot vérifié.
     *
     * @param mot le mot vérifié
     */
    public void ajouterMotVerifie(MotVerifie mot) {
        if (mot.isAccepte()) {
            mots.ajouterChat(String.format("Vous avez trouvé \"%s\" (+%dpts).", mot.getMot(), mot.getPoints()));
        } else {
            mots.ajouterChat(String.format("Le mot %s n'est pas valide.", mot.getMot()));
        }
    }

    /**
     * Affiche les informations du mot trouvé.
     *
     * @param mot le mot trouvé
     */
    public void ajouterMotTrouve(MotTrouve mot) {
        mots.ajouterChat(String.format("%s a trouvé un mot", mot.getNom()));
    }

    /**
     * Affiche les informations de fin de manche.
     *
     * @param finManche les informations de fin de manche
     */
    public void finManche(FinManche finManche) {
        var joueurs = Arrays.asList(finManche.getJoueurs());
        joueurs.sort((j1, j2) -> j1.getPoints() - j2.getPoints());
        joueurs.forEach(j -> ajouterChat(String.format("%s a %d points.", j.getNom(), j.getPoints())));
        ajouterChat("Manche finie, la prochaine manche commence dans 10 secondes.");
    }
}
