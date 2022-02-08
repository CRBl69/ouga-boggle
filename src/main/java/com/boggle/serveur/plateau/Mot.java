package com.boggle.serveur.plateau;

import java.util.LinkedList;

public class Mot {
  private LinkedList<Lettre> lettres;

  public Mot(LinkedList<Lettre> lettres) {
    this.lettres = lettres;
  }

  public LinkedList<Lettre> getLettres() {
    return lettres;
  }
}
