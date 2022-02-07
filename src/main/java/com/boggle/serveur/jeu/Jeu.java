package com.boggle.serveur.jeu;

import java.util.ArrayList;
import java.util.HashMap;

import com.boggle.serveur.plateau.Grille;
import com.boggle.serveur.plateau.Mot;

/** fonctions relatives à la partie */
public class Jeu {
  private HashMap<Joueur, ArrayList<Mot>> listeMots;
  private ArrayList<Joueur> joueurs;
  private Grille grille;
  private int nombreManche;
  private int mancheEffectuees;

  public Jeu (int nombreManche, Grille grille) {
    this.listeMots = new HashMap<>();
    this.joueurs = new ArrayList<>();
    this.grille = grille;
    if(nombreManche < 1) {
      throw new IllegalArgumentException("le nombre de nombreManche doit être supérieur ou égal à 1");
    } else {
      this.nombreManche = nombreManche;
    }
  }

  /**
   * @return true si on a effectuer le nombre de manche 
   * voulue
   */
  public boolean finDePartie() {
    return nombreManche == mancheEffectuees;

  }

  /**
   * @return true si le timer a atteint 0 sinon false
   */
  public boolean finDeManche() {
    // TODO: implémenter la condition d'arret de la partie
    // true quand le timer a atteint 0
    // sinon false
    return true;
  }

  /**
   * incrémente le valeur de mancheEffectuees
   */
  public void incrementeMancheEffectuer() {
    mancheEffectuees++;
  }

  /**
   * Ajoute un mot valide dans la liste de mot trouvés par
   * le joueur
   * @param mot mot qui a été trouvé
   * @param joueur joueur qui a trouvé le mot
   */
  public void ajouterMotTrouve(Mot mot, Joueur joueur) {
    if(!listeMots.containsKey(joueur)) {
      listeMots.put(joueur, new ArrayList<>());
    }
    listeMots.get(joueur).add(mot);
  }

  /**
   * réinitialise le nombre de point et la liste de mot
   */
  public void réinitialisePartie() {
    reinitialiseListeMots();
    reinitialisePoint();
  }

  /** 
   * réinitialise la liste de mot 
   */
  public void reinitialiseListeMots() {
    listeMots.clear();
  }

  /**
   * réinitialise le nombre de points de chaque joueur
   */
  public void reinitialisePoint() {
    for(Joueur joueur : joueurs) {
      joueur.reinitialisePoint();
    }
  }
  
  /**
   * 
   * @return le(s) joueur(s) qui a le plus de points
   */
  public ArrayList<Joueur> joueursGagnant() {
    ArrayList<Joueur> gagnant = new ArrayList<>();
    int max = 0;
    for(Joueur joueur : listeMots.keySet()) {
      calculePoint(joueur);
      if(max < joueur.getNombrePoints()) {
        // ici on est dans le cas où on trouve un joueur
        // qui pourrait être le gagnant
        max = joueur.getNombrePoints();
        gagnant.clear();
        gagnant.add(joueur);
      } else if(max == joueur.getNombrePoints()) {
        // ici on est dans le cas où il peux y avoir une égalité
        // entre le joueur et le gagnant
        max = joueur.getNombrePoints();
        gagnant.add(joueur);
      }
    }
    return gagnant;
  }

  /**
   * Calcule le point total du joueur en parcourant la liste de 
   * mot trouvés et l'assigne à la variable nombrePoints de joueur
   */
  public void calculePoint(Joueur joueur) {
    int acc = 0;
    if(listeMots.containsKey(joueur)) {
      for(Mot mot : listeMots.get(joueur)) {
        switch(mot.getLettres().size()){
          case 3:
          case 4: acc += 1; break;
          case 5: acc += 2; break;
          case 6: acc += 3; break;
          case 7: acc += 5; break;
          default: acc += 11; break;
        }
      }
    }
    joueur.setNombrePoints(acc);
  }

  public void ajouterJoueur(Joueur joueur) {
    joueurs.add(joueur);
  }

  public void enleverJoueur(Joueur joueur) {
    joueurs.remove(joueur);
  }

  public ArrayList<Joueur> getJoueurs() {
    return joueurs;
  }
}
