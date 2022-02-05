package com.boggle.serveur.plateau;

/** les jetons lettres */
public class Lettre {
    public final String lettre;
    public final Coordonnee coord;

    public Lettre (Coordonnee coord, String lettre) {
        this.coord = coord;
        this.lettre = lettre;
    }

    public boolean estACoteDe (Lettre lettre) {
        return this.coord.estACoteDe(lettre.coord);
    }
}
