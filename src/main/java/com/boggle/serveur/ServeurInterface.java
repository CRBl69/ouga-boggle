package com.boggle.serveur;

public interface ServeurInterface {
    public void annoncerDebutPartie();

    public void annoncerFinPartie();

    public void annoncerDebutManche();

    public void annoncerFinManche();

    public void annoncerMotTrouve(String nom);

    public void annoncerElimination(String nom);
}
