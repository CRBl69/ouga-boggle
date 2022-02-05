package com.boggle.serveur.jeu;

import java.util.ArrayList;
import java.util.LinkedList;

/** contient les informations relatives au joueur */
public class Joueur {
  private int nb_point;
  public String nom;
  private ArrayList<String> list_mot;
  private LinkedList<Integer> tenLastPoint;

  public Joueur(String nom) {
    this.nom = nom;
    nb_point = 0;
    list_mot = new ArrayList<>();
    tenLastPoint = new LinkedList<>();
  }

  /**
   * Ajoute un mot valide dans la liste de mot trouvée par
   * le joueur tout au long de la partie
   * @param mot mot qui a été trouvé par le joueur
   */
  public void ajoutMotTrouver(String mot) {
    list_mot.add(mot);
  }

  /**
   * @return la liste de mot qui a été trouvé par le joueur
   */
  public ArrayList<String> motDansListe() {
    return new ArrayList<String>(list_mot);
  }

  /**
   * réinitialise le nombre de point et la liste de mot
   */
  public void resetPartie() {
    resetListMot();
    resetPoint();
  }

  /**
   * retire tous les mot trouvés de la liste de mot
   */
  private void resetListMot() {
    list_mot.clear();
  }

  /**
   * Ajout du score de la dernière partie dans la liste 
   * tenLastPoint avant de remettre le nombre de point à 0
   */
  private void resetPoint() {
    ajoutPointDernierPartie();
    nb_point = 0;
  }

  /**
   * Ajoute le score de la dernière partie dans la liste 
   * tenLastPoint. On veut seulement les scores des 10
   * parties précédentes donc on s'assure que la taille 
   * de la liste tenLastPoint ne doit pas dépasser 10
   */
  private void ajoutPointDernierPartie() {
    if(tenLastPoint.size() == 10) {
      tenLastPoint.removeLast();
    }
    tenLastPoint.addFirst(nb_point);
  }

  /**
   * calcule le point total du joueur de la liste de mot
   * trouvé
   */
  public int calculePoint() {
    for(String mot : list_mot) {
      switch(mot.length()){
        case 3:
        case 4: nb_point += 1; break;
        case 5: nb_point += 2; break;
        case 6: nb_point += 3; break;
        case 7: nb_point += 5; break;
        default: nb_point += 8; break;
      }
    }
    return nb_point;
  }

}
