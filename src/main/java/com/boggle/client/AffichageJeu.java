package com.boggle.client;

import com.boggle.client.Client.Serveur;
import com.boggle.client.affichage.*;
import com.boggle.serveur.messages.Continue;
import com.boggle.serveur.messages.DebutJeu;
import com.boggle.serveur.messages.DebutManche;
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
    private Serveur serveur;
    private DebutJeu debutJeu;
    private int nbManchesEcoulees;
    private JPanel infoPanel = new JPanel(new GridLayout(1, 2));

    private VueEntreeTexte entreeTexte = new VueEntreeTexte(mot -> serveur.envoyerMotClavier(mot));
    private VueGrille grille = new VueGrille(entreeTexte, mot -> serveur.envoyerMotSouris(mot));
    private VueMinuteur minuteur = new VueMinuteur();
    private VueInfos infos;
    public VueChat chat = new VueChat();

    Consumer<Mot> action;

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
     * @param serveur le serveur avec lequel communiquer
     * @param debutJeu les informations de début de jeu
     */
    public AffichageJeu(Serveur serveur, DebutJeu debutJeu) {
        super();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.serveur = serveur;

        this.debutJeu = debutJeu;

        this.infos = new VueInfos(this.debutJeu.getNbManches());

        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        panel.add(grille, gbc);

        entreeTexte.addActionListener((a) -> entreeTexte.setText(""));

        gbc.gridy = 4;
        gbc.gridheight = 1;
        panel.add(entreeTexte, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        panel.add(chat, gbc);

        infoPanel.add(minuteur);
        infoPanel.add(infos);

        gbc.gridy = 4;
        gbc.gridheight = 1;
        panel.add(infoPanel, gbc);

        pack();
        setVisible(true);
    }

    /**
     * Constructeur pour une partie en cours.
     * @param serveur le serveur avec lequel communiquer
     * @param continuePartie les informations pour continuer la partie
     */
    public AffichageJeu(Serveur serveur, Continue continuePartie) {
        this(serveur, continuePartie.getDebutJeu());

        SwingUtilities.invokeLater(() -> {
            infoPanel.remove(infos);
            infos = new VueInfos(continuePartie.getDebutJeu().getNbManches(), continuePartie.getNombreManchesJouees());
            infos.updateStatus(continuePartie.partieEstdemaree());
            infos.ajouterPoints(continuePartie.getPoints());
            for (int i = 0; i <= continuePartie.getNombreManchesJouees(); i++) {
                infos.updateManches();
            }
            infoPanel.add(infos);
        });

        DebutManche dm = new DebutManche(continuePartie.getTableau(), continuePartie.getTempsRestant());
        initManche(dm);
        activerInterface(continuePartie.partieEstdemaree());
        nbManchesEcoulees = continuePartie.getNombreManchesJouees();
        infos.ajouterPoints(continuePartie.getPoints());
    }

    /**
     * Prépare l'interface pour une nouvelle manche.
     *
     * @param lettres nouvelles lettres
     */
    public void initManche(DebutManche dm) {
        grille.miseAJour(dm.getTableau());
        this.activerInterface(true);
        infos.updateStatus(true);
        infos.updateManches();
        entreeTexte.grabFocus();
        grille.updateUI();
        minuteur.start(dm.getLongueurManche());
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
        chat.ajouterChat(message);
    }

    /**
     * Affiche les informations du mot vérifié.
     *
     * @param mot le mot vérifié
     */
    public void ajouterMotVerifie(MotVerifie mot) {
        if (mot.isAccepte()) {
            infos.ajouterPoints(mot.getPoints());
            chat.ajouterChat(String.format("Vous avez trouvé \"%s\" (+%dpts).", mot.getMot(), mot.getPoints()));
            jouerSonDeVictoire();
        } else {
            chat.ajouterChat(String.format("Le mot %s n'est pas valide.", mot.getMot()));
            jouerSonDeDefaite();
        }
    }

    /**
     * Affiche les informations du mot trouvé.
     *
     * @param mot le mot trouvé
     */
    public void ajouterMotTrouve(MotTrouve mot) {
        chat.ajouterChat(String.format("%s a trouvé un mot", mot.getPseudo()));
    }

    public void activerInterface(boolean activer) {
        grille.activer(activer);
        entreeTexte.activer(activer);
    }

    /**
     * Affiche les informations de fin de manche.
     *
     * @param finManche les informations de fin de manche
     */
    public void finManche(FinManche finManche) {
        this.activerInterface(false);
        infos.updateStatus(false);
        var joueurs = Arrays.asList(finManche.getJoueurs());
        joueurs.sort((j1, j2) -> j1.getPoints() - j2.getPoints());
        joueurs.forEach(j -> ajouterChat(String.format("%s a %d points.", j.getNom(), j.getPoints())));
        nbManchesEcoulees++;
        if (nbManchesEcoulees == debutJeu.getNbManches()) {
            ajouterChat("Fin de la partie. Victoire de "
                    + joueurs.get(joueurs.size() - 1).getNom() + ".");
        } else {
            ajouterChat("Manche finie, la prochaine manche commence dans 10 secondes.");
        }
    }

    /*
     * FIXME: faire marcher le son.
     *
     * Temps passśe sur ce problème : 2h30.
     *
     * Vous êtes priées de mettre à jour le compteur de temps
     * lorsque vous perdez votre temps sur ce problème.
     */
    private void jouerSonDeDefaite() {
        // Util.playSound("defaite.mp3");
    }

    private void jouerSonDeVictoire() {
        // Util.playSound("victoire.mp3");
    }
}
