package com.boggle.serveur.jeu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.boggle.serveur.plateau.Mot;

public class Historique implements Serializable{
    private List<HashMap<Joueur, DonneeJoueur>> manches = new ArrayList<HashMap<Joueur, DonneeJoueur>>();

    public Historique(List<Manche> manches) {
        List<HashMap<Joueur, HashSet<Mot>>> manchesMap = new ArrayList<>();
        manches.forEach(m -> {
            manchesMap.add(m.getMots());
        });
        int index = 0;
        for (HashMap<Joueur, HashSet<Mot>> manche : manchesMap) {
            this.manches.add(new HashMap<Joueur, DonneeJoueur>());
            for (Joueur joueur : manche.keySet()) {
                DonneeJoueur donnee = new DonneeJoueur(manche.get(joueur));
                this.manches.get(index).put(joueur, donnee);
            }
            index++;
        }
    }
}

class DonneeJoueur {
    private int points;
    private HashSet<Mot> mots;

    public DonneeJoueur(HashSet<Mot> mots) {
        this.mots = mots;
        // this.points = mots.stream().mapToInt(Mot::getPoints).sum();
    }
}