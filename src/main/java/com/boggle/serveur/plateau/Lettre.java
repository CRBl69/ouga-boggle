package com.boggle.serveur.plateau;

/** les jetons lettres */
public class Lettre {
    public final String lettre;
    public final Coordonnee coord;

    /**
     * Constructeur.
     *
     * @param coord coordonnées pour suivre son emplacement.
     * @param lettre caractère pour la lettre.
     */
    public Lettre(Coordonnee coord, String lettre) {
        this.coord = coord;
        this.lettre = lettre;
    }

    public Lettre(String lettre) {
        this.coord = null;
        this.lettre = lettre;
    }

    /**
     * Verifie si deux lettres sont adjacentes.
     *
     * @param lettre caractère pour la lettre.
     * @return boolean true si les deux lettres sont adjacentes.
     */
    public boolean estACoteDe(Lettre lettre) {
        return this.coord.estACoteDe(lettre.coord);
    }

    public boolean estSur(Lettre lettre) {
        return this.coord.estSur(lettre.coord);
    }

    public String getString() {
        return lettre;
    }
}
