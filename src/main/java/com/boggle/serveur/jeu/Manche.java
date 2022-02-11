package com.boggle.serveur.jeu;

import java.util.HashMap;
import java.util.HashSet;

import com.boggle.serveur.plateau.Grille;
import com.boggle.serveur.plateau.Mot;

/** utiliser pour la sauvegarde des manches */
public class Manche {
  private Grille grille;
  private HashMap<Joueur, HashSet<Mot>> listeMots;
  private Minuteur minuteur;
  private Joueur joueurGagnant;
  
  public Manche(Grille grille, HashMap<Joueur, HashSet<Mot>> listeMots, Minuteur minuteur, Joueur joueurGagnant) {
    this.grille = grille;
    this.listeMots = listeMots;
    this.minuteur = minuteur;
    this.joueurGagnant = joueurGagnant;
  }

  public Grille getGrille() {
    return grille;
  }

  public HashMap<Joueur, HashSet<Mot>> getListeMots() {
    return listeMots;
  }

  public Minuteur getMinuteur() {
    return minuteur;
  }

  public Joueur getJoueurGagnant() {
    return joueurGagnant;
  }

}
