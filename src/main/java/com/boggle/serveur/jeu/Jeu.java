package com.boggle.serveur.jeu;

import java.util.ArrayList;

import com.boggle.serveur.plateau.Grille;

/** fonctions relatives à la partie */
public class Jeu {
  private Joueur[] joueurs;
  // private Grille grille;
  private int nb_manche;
  private int mancheEffectuer;

  public Jeu (Joueur[] joueurs, int nb_manche) {
    this.joueurs = joueurs;
    // TODO: générer la grille
    // this.grille = genGrille();

    if(nb_manche < 1) {
      throw new IllegalArgumentException("le nombre de nb_manche doit être supérieur ou égal à 1");
    } else {
      this.nb_manche = nb_manche;
    }
  }

  /**
   * @return true si le timer a atteint 0 sinon false
   */
  public boolean finDePartie() {
    // TODO: implémenter la condition d'arret de la partie
    // true quand le timer a atteint 0
    // sinon false
    return true;
  }

  /**
   * @return true si on a effectuer le nombre de manche 
   * voulue
   */
  public boolean finDeManche() {
    return nb_manche == mancheEffectuer;
  }

  /**
   * incrémente le valeur de nb_manche effectué
   */
  public void incrementeMancheEffectuer() {
    mancheEffectuer++;
  }

  /**
   * @return les(s) joueur(s) qui a le plus de points
   */
  public ArrayList<Joueur> joueurGagnant() {
    ArrayList<Joueur> gagnant = new ArrayList<>();
    int max = joueurs[0].calculePoint();
    for(int i=1; i<joueurs.length; i++) {
      if(max < joueurs[i].calculePoint()) {
        // dans le cas où on trouve un potentiel joueur
        // qui a plus de point
        max = joueurs[i].calculePoint();
        gagnant.clear();
        gagnant.add(joueurs[i]);

      } else if(max == joueurs[i].calculePoint()) {
        // dans le cas où joueurs[i] a le même point que
        // le potentiel joueur qui a le plus de points
        max = joueurs[i].calculePoint();
        gagnant.add(joueurs[i]);
      }
    }
    return gagnant;
  }

  public Joueur[] getJoueurs() {
    return joueurs;
  }
}
