package com.boggle.util;

import java.util.LinkedList;

public class Mot {
    private LinkedList<Coordonnee> coordonnees;

    public Mot() {
        this.coordonnees = new LinkedList<>();
    }

    public LinkedList<Coordonnee> getCoordonnees() {
        return coordonnees;
    }

    public void pushLettre(Coordonnee coordonnee) {
        this.coordonnees.add(coordonnee);
    }
}
