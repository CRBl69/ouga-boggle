package com.boggle.serveur.jeu;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SauvegardeClassement extends Sauvegarde{
    private HashMap<Joueur, Integer> Classement;
    private int points;
    private Joueur joueur;

    public SauvegardeClassement(String chemin) {
        this.chemin = chemin;
    }

    @Override
    public void sauvegarder() {
        if(joueur == null)
            throw new IllegalArgumentException();
        charger();
        Classement.put(joueur, points);
        try {
            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(chemin));
            obj.writeObject(Classement);
            obj.close();
        } catch (Exception i) {
            i.printStackTrace();
        }
    }
    
    @Override
    public void charger() {
        try { 
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(chemin));
            Classement = (HashMap<Joueur, Integer>) obj.readObject();
            obj.close();
        } catch (Exception i) {
            Classement = new HashMap<Joueur, Integer>();
        }
    }
    
    public HashMap<Joueur, Integer> getClassement() {
        return Classement;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    
    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }
    
}
