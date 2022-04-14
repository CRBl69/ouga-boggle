package com.boggle.serveur;

import com.boggle.serveur.jeu.ConfigurationServeur;
import com.boggle.serveur.jeu.Jeu;
import com.boggle.serveur.jeu.Joueur;
import com.boggle.serveur.jeu.modes.*;
import com.boggle.serveur.messages.*;
import com.boggle.serveur.plateau.Lettre;
import com.boggle.util.Logger;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

/** Gère la communication avec tous les clients. */
public class Serveur implements ServeurInterface {
    private Logger logger = Logger.getLogger("SERVEUR");
    private ServerSocket serveur;
    private ArrayList<Client> clients = new ArrayList<>();
    private String motDePasse;
    private Gson gson = new Gson();
    private Jeu jeu;
    private ConfigurationServeur configuration;
    private ConfigurationJeu configurationJeu;
    private Client chefDeLobby;

    public Serveur(ConfigurationServeur c) throws IOException {
        this.configuration = c;
        this.motDePasse = c.mdp;
        serveur = new ServerSocket(c.port);
        demarerServeur();
    }

    public void lancerPartie(ConfigurationJeu c) {
        configurationJeu = c;
        jeu = switch (c.modeDeJeu) {
            case BATTLE_ROYALE -> new BattleRoyale(c.timer, c.tailleGrilleV, c.tailleGrilleH, c.langue, this);
            default -> new Normal(c.nbManches, c.timer, c.tailleGrilleV, c.tailleGrilleH, c.langue, this);};
        jeu.demarrerJeu();
    }

    /** Démarre le serveur. */
    public void demarerServeur() {
        while (true) {
            Socket s = null;
            try {
                s = serveur.accept();
                logger.info("Nouveau client connecté");

                Client client = poigneeDeMain(s);
                if (client != null) {
                    annoncerNouveauClient(client);
                    if (clients.size() == 0) {
                        chefDeLobby = client;
                    }
                    clients.add(client);
                    Thread t = new GestionnaireClient(client);
                    t.start();
                }
            } catch (IOException e) {
                try {
                    s.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    /**
     * Effectue une poignée de main avec le client.
     *
     * @param s socket du client
     * @return le client si la poignée de main est valide, null sinon
     */
    private Client poigneeDeMain(Socket s) throws IOException {
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // On demande un nom et un mot de passe au client
        String nom = dis.readUTF();
        String motDePasse = dis.readUTF();

        if (clients.stream().anyMatch(c -> c.joueur.nom.equals(nom))) {
            dos.writeUTF("NOPE");
            dos.close();
            dis.close();
            s.close();
            return null;
        }

        if (motDePasse.equals(this.motDePasse)) {
            if (jeu != null && jeu.estCommence()) {
                if (jeu.getJoueurs().stream().anyMatch(j -> j.nom.equals(nom))) {
                    Continue continueMessage = new Continue(
                            lettresToString(jeu.getMancheCourante().getGrille().getGrille()),
                            jeu.mancheEnCours(),
                            (int) jeu.getMancheCourante().getMinuteur().tempsRestant(),
                            new DebutJeu(configurationJeu.nbManches),
                            jeu.getNombreManche() - 1,
                            jeu.getPoints().getOrDefault(new Joueur(nom), 0));
                    dos.writeUTF(
                            String.format("OK %s", gson.toJson(new PoigneeDeMain(clients, true, continueMessage))));
                    return new Client(s, dos, dis, nom);
                } else {
                    dos.writeUTF("NOPE");
                    dos.close();
                    dis.close();
                    s.close();
                    return null;
                }
            } else {
                dos.writeUTF(String.format("OK %s", gson.toJson(new PoigneeDeMain(clients, false, null))));
                return new Client(s, dos, dis, nom);
            }
        } else {
            dos.writeUTF("NOPE");
            dos.close();
            dis.close();
            s.close();
            return null;
        }
    }

    private void annoncerMessage(Chat message) {
        annoncer("message " + gson.toJson(message));
    }

    private void annoncerNouveauClient(Client c) {
        annoncer(String.format("connexion %s", gson.toJson(new Connexion(c.joueur.nom, true))));
    }

    private void annoncerDeconnextion(Client c) {
        annoncer(String.format("connexion %s", gson.toJson(new Connexion(c.joueur.nom, false))));
    }

    private void annoncerStatus(Status status) {
        annoncer("status " + gson.toJson(status));
    }

    public void annoncerDebutPartie() {
        annoncer("debutJeu " + gson.toJson(new DebutJeu(configurationJeu.nbManches)));
    }

    public void annoncerFinPartie() {
        annoncer("finJeu " + gson.toJson(new FinPartie(jeu.getJoueurGagnant())));
    }

    public void annoncerDebutManche() {
        annoncer("debutManche "
                + gson.toJson(new DebutManche(
                        jeu.getGrille(), jeu.getMancheCourante().getMinuteur().getSec(), jeu.getJoueurs())));
    }

    public void annoncerFinManche() {
        annoncer("finManche " + gson.toJson(new FinManche(jeu)));
    }

    public void annoncerMotTrouve(String nom) {
        annoncer("motTrouve " + gson.toJson(new MotTrouve(nom)));
    }

    public void annoncerElimination(String nom) {
        annoncer("elimination " + nom);
    }

    /**
     * Annonce un message à tous les clients.
     *
     * @param message message à annoncer
     */
    public void annoncer(String message) {
        for (Client c : clients) {
            c.envoyerMessage(message);
        }
    }

    /**
     * @param nouveauMot le nouveau mot souris à ajouter
     * @param client le client qui a ajouté le mot
     */
    private void ajouterMot(NouveauMotSouris nouveauMot, Client client) {
        LinkedList<Lettre> liste = new LinkedList<>();
        for (Lettre l : nouveauMot.getLettres()) {
            liste.add(jeu.getMancheCourante().getGrille().getGrille()[l.coord.x][l.coord.y]);
        }
        var joueurOpt = jeu.getJoueurs().stream()
                .filter(j -> j.nom.equals(nouveauMot.getPseudo()))
                .findFirst();
        if (joueurOpt.isPresent()) {
            int valide = jeu.ajouterMot(liste, joueurOpt.get());
            if (valide > 0) {
                logger.info("Mot valide");
                annoncerMotTrouve(nouveauMot.getPseudo());
            } else {
                logger.info("Mot invalide");
            }
            client.envoyerMessage("motVerifie " + gson.toJson(new MotVerifie(nouveauMot.getId(), valide > 0, valide)));
        }
    }

    /**
     * @param nouveauMot le nouveau mot clavier à ajouter
     * @param client le client qui a ajouté le mot
     */
    private void ajouterMot(NouveauMotClavier nouveauMot, Client client) {
        int valide = jeu.ajouterMot(
                nouveauMot.getMot(),
                jeu.getJoueurs().stream()
                        .filter(j -> j.nom.equals(nouveauMot.getPseudo()))
                        .findFirst()
                        .get());
        if (valide > 0) {
            logger.info("Mot valide");
            annoncerMotTrouve(nouveauMot.getPseudo());
        } else {
            logger.info("Mot invalide");
        }
        client.envoyerMessage("motVerifie " + gson.toJson(new MotVerifie(nouveauMot.getId(), valide > 0, valide)));
    }

    /** Classe qui gère les messages envoyés par le client. */
    private class GestionnaireClient extends Thread {
        private Client client;

        public GestionnaireClient(Client c) {
            this.client = c;
        }

        public void run() {
            String ligne = "";
            boolean exit = false;
            while (true) {
                try {
                    ligne = client.dis.readUTF();
                    logger.info("Message reçu : " + ligne);
                    String motClef = ligne.split(" ")[0];
                    String donnees = "";
                    if (!motClef.equals(ligne)) {
                        donnees = ligne.substring(motClef.length() + 1);
                    }
                    switch (motClef) {
                        case "message":
                            Chat chat = gson.fromJson(donnees, Chat.class);
                            chat.setPseudo(client.joueur.nom);
                            annoncerMessage(chat);
                            break;
                        case "status":
                            if (jeu != null && jeu.estCommence()) break;
                            Status status = gson.fromJson(donnees, Status.class);
                            status.setPseudo(client.joueur.nom);
                            annoncerStatus(status);

                            client.joueur.estPret = status.getStatus();
                            if (tousLesClientsSontPrets() && configurationJeu != null) {
                                lancerPartie(configurationJeu);
                                clients.forEach(c -> {
                                    jeu.ajouterJoueur(c.joueur);
                                });
                            }
                            break;
                        case "motSouris":
                            NouveauMotSouris mot = gson.fromJson(donnees, NouveauMotSouris.class);
                            mot.setPseudo(client.joueur.nom);
                            ajouterMot(mot, client);
                            break;
                        case "motClavier":
                            NouveauMotClavier motClavier = gson.fromJson(donnees, NouveauMotClavier.class);
                            motClavier.setPseudo(client.joueur.nom);
                            ajouterMot(motClavier, client);
                            break;
                        case "config":
                            ConfigurationJeu configJeu = gson.fromJson(donnees, ConfigurationJeu.class);
                            if (client == chefDeLobby) {
                                configurationJeu = configJeu;
                            }
                            if (tousLesClientsSontPrets()) {
                                lancerPartie(configurationJeu);
                                clients.forEach(c -> {
                                    jeu.ajouterJoueur(c.joueur);
                                });
                            }
                            break;
                        case "continue":
                            break;
                        case "stop":
                            exit = true;
                            break;
                        default:
                            logger.warn(motClef + " n'est pas reconnu");
                            break;
                    }
                    if (exit) {
                        logger.warn("Client \"" + client.joueur.nom + "\" s'est déconnecté");
                        annoncerDeconnextion(client);
                        client.arreter();
                        clients.remove(client);
                        break;
                    }
                } catch (IOException e) {
                    logger.warn("Perte de connexion du client \"" + client.joueur.nom + "\"");
                    annoncerDeconnextion(client);
                    clients.remove(client);
                    break;
                }
            }
        }
    }

    /**
     * Classe qui représente un client.
     *
     * Elle stocke son socket, son output stream, son input stream, son status
     * et son pseudo.
     */
    public class Client {
        public final DataOutputStream dos;
        public final DataInputStream dis;
        public final Socket s;
        public final Joueur joueur;

        private Client(Socket s, DataOutputStream dos, DataInputStream dis, String nom) {
            this.s = s;
            this.dos = dos;
            this.dis = dis;
            this.joueur = new Joueur(nom);
        }

        /**
         * Envoie un message à ce client.
         *
         * @param message message à envoyer
         */
        public void envoyerMessage(String s) {
            try {
                this.dos.writeUTF(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("Message envoyé : " + s);
        }

        /**
         * Arrête la connexion au client.
         */
        private void arreter() {
            try {
                this.s.close();
                this.dos.close();
                this.dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tousLesClientsSontPrets() {
        return clients.stream().allMatch(c -> c.joueur.estPret);
    }

    public void stop() {
        try {
            clients.forEach(c -> c.arreter());
            serveur.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[][] lettresToString(Lettre[][] lettres) {
        String[][] lettresString = new String[lettres.length][lettres[0].length];
        for (int i = 0; i < lettres.length; i++) {
            for (int j = 0; j < lettres[0].length; j++) {
                lettresString[i][j] = lettres[i][j].toString();
            }
        }
        return lettresString;
    }

    /** à la fin du jeu, rend tous les clients non pret et remet le jeu à null*/
    public void finirJeu() {
        clients.forEach(c -> {
            c.joueur.estPret = false;
            annoncerStatus(new Status(false, c.joueur.nom));
        });
        jeu = null;
    }
}
