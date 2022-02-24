package com.boggle.serveur.jeu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class SauvegardeHistorique extends Sauvegarde{
    private LinkedList<Historique> Historique; 
    private Historique dernierePartie;

    public SauvegardeHistorique(String chemin) {
        this.chemin = chemin;
    }

    @Override
    public void sauvegarder() {
        if(dernierePartie == null) 
            throw new IllegalArgumentException();
        charger();
        Historique.add(dernierePartie);
        try {
            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(chemin));
            obj.writeObject(Historique);
            obj.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    @Override
    public void charger() {
        try { 
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(chemin));
            Historique = (LinkedList<Historique>) obj.readObject();
            obj.close();
        } catch (IOException | ClassNotFoundException i) {
            Historique = new LinkedList<>();
        }
    }

    public LinkedList<Historique> getHistorique() {
        return Historique;
    }

    public void setDernierePartie(Historique dernierePartie) {
        this.dernierePartie = dernierePartie;
    }
}
