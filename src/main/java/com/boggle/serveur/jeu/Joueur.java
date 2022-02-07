package com.boggle.serveur.jeu;

/** contient les informations relatives au joueur */
public class Joueur {
  private int nombrePoints;
  public String nom;

  public Joueur(String nom) {
    this.nom = nom;
    nombrePoints = 0;
  }

  /**
   * réinitialise le nombre de points du joueur
   */
  public void reinitialisePoint() {
    nombrePoints = 0;
  }

  public int getNombrePoints() {
    return nombrePoints;
  }

  public void setNombrePoints(int points) {
    nombrePoints = points;
  } 

}
