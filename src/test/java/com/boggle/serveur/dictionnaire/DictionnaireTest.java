package com.boggle.serveur.dictionnaire;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.boggle.serveur.jeu.Langue;
import org.junit.Test;

/**
 * Test de la classe Dictionnaire.
 */
public class DictionnaireTest {
    @Test
    public void trouveLesMotsFrancais() {
        Dictionnaire.genererArbre(Langue.FR);
        assertTrue(Dictionnaire.estUnMot("chapeau"));
        assertTrue(Dictionnaire.estUnMot("cheval"));
        assertTrue(Dictionnaire.estUnMot("a"));
        assertFalse(Dictionnaire.estUnMot("hfaghd"));
        assertFalse(Dictionnaire.estUnMot("friend"));
        assertFalse(Dictionnaire.estUnMot(""));
    }

    @Test
    public void trouveLesMotsAnglais() {
        Dictionnaire.genererArbre(Langue.EN);
        assertTrue(Dictionnaire.estUnMot("hat"));
        assertTrue(Dictionnaire.estUnMot("hello"));
        assertFalse(Dictionnaire.estUnMot("anglais"));
        assertFalse(Dictionnaire.estUnMot(""));
    }

    @Test
    public void trouveLesMotsEspagnols() {
        Dictionnaire.genererArbre(Langue.ES);
        assertTrue(Dictionnaire.estUnMot("senorita"));
        assertTrue(Dictionnaire.estUnMot("embicadura"));
        assertFalse(Dictionnaire.estUnMot("bonjour"));
        assertFalse(Dictionnaire.estUnMot(""));
    }

    @Test
    public void trouveLesMotsAllemands() {
        Dictionnaire.genererArbre(Langue.DE);
        //assertTrue(Dictionnaire.estUnMot("mitteilenswerter"));
        assertTrue(Dictionnaire.estUnMot("Verfahrensstufe"));
        assertFalse(Dictionnaire.estUnMot("bonjour"));
        assertFalse(Dictionnaire.estUnMot(""));
    }
}
