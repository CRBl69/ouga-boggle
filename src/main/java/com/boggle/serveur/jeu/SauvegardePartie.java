package com.boggle.serveur.jeu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SauvegardePartie extends Sauvegarde{
    private Partie partie;
    private Partie partieCharge;

    public SauvegardePartie(String chemin) {
        this.chemin = chemin;
    }

    @Override
    public void sauvegarder() {
        if(partie == null)
            throw new IllegalArgumentException();
        try {
            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(chemin));
            obj.writeObject(partie);
            obj.close();
        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    @Override
    public void charger() {
        try { 
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(chemin));
            partieCharge = (Partie) obj.readObject();
            obj.close();
        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    public Partie getPartieCharge() {
        return partieCharge;
    }

    public void setPartie(Partie partie) {
        this.partie = partie;
    }
    
}
