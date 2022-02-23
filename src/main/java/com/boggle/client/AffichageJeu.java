package com.boggle.client;

import com.boggle.serveur.plateau.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.swing.*;

/** Vue du jeu */
public class AffichageJeu extends JFrame {
  private ChampDeTexte champDeTexte = new ChampDeTexte();
  private VueGrille grille;
  private VueMots mots = new VueMots();

  Consumer<Mot> action;

  private class ChampDeTexte extends JTextField {
    public ChampDeTexte() {
      super();

      addActionListener(
          (ac) -> {
            LinkedList<Lettre> mot =
                ac.getActionCommand()
                    .chars()
                    // IntStream
                    .mapToObj(c -> Character.toString((char) c))
                    // Stream<String> // TODO: calculer la position de chaque lettre
                    .map(c -> new Lettre(new Coordonnee(0, 0), c))
                    // Stream<Lettre>
                    .collect(Collectors.toCollection(LinkedList::new));

            if (action != null) action.accept(new Mot(mot));
          });
    }
  }

  /** Vue pour la grille de lettres */
  private class VueGrille extends JPanel {
    int hauteur;
    int largeur;
    VueCase[] contenu;

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
     * Constructeur.
     *
     * @param g grille de lettres a utiliser
     */
    public VueGrille(String[] g) {
      super(new GridLayout(g[0].length(), g.length));
      largeur = g[0].length();
      hauteur = g.length;
      contenu = new VueCase[largeur * hauteur];

      for (int i = 0; i < largeur; i++) {
        for (int j = 0; j < hauteur; j++) {
          VueCase c = new VueCase(g[j].substring(i, i + 1));
          contenu[j * largeur + i] = c;
          add(c);
        }
      }
    }
  }

  /** Vue pour les mots trouves */
  private class VueMots extends JPanel {

    /** Constructeur. */
    public VueMots() {
      super();
      setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    /**
     * Ajoute un mot a la liste de mots
     *
     * @param mot le mot a ajouter
     */
    public void ajouterChat(String message) {
      // On ne peut pas utiliser .contains avec des String
      add(new JLabel(message));
    }
  }

  /**
   * Constructeur.
   *
   * @param lettres la grille de lettres a utiliser j
   */
  public AffichageJeu(String[] lettres) {
    super();
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    JPanel panel = new JPanel(new GridBagLayout());
    add(panel);

    GridBagConstraints gbc = new GridBagConstraints();

    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = 0;
    gbc.insets = new Insets(10, 10, 10, 10);

    grille = new VueGrille(lettres);

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
    pack();
  }
}
