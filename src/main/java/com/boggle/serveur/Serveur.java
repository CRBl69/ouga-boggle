package com.boggle.serveur;

import com.boggle.serveur.jeu.ConfigurationServeur;
import com.boggle.serveur.jeu.Jeu;
import com.boggle.serveur.jeu.Joueur;
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

    public Serveur(ConfigurationServeur c) throws IOException {
        this.configuration = c;
        this.motDePasse = c.mdp;
        serveur = new ServerSocket(c.port);
        jeu = new Jeu(c.nbManches, c.timer, c.tailleGrilleV, c.tailleGrilleH, c.langue, this);
        demarerServeur();
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

        if (motDePasse.equals(this.motDePasse)) {
            long nPrets = clients.stream().filter((c) -> c.pret).count();
            int nClients = clients.size() + 1;
            dos.writeUTF(String.format("OK %d %d", nPrets, nClients));
            return new Client(s, dos, dis, nom);
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
        annoncer(String.format("connexion {\"nom\": \"%s\"}", c.nom));
    }

    private void annoncerDeconnextion(Client c) {
        annoncer(String.format("deconnexion {\"nom\": \"%s\"}", c.nom));
    }

    private void annoncerStatus(Status status) {
        annoncer("status " + gson.toJson(status));
    }

    public void annoncerDebutPartie() {
        annoncer("debutJeu " + gson.toJson(new ConfigDebut(configuration.nbManches, configuration.timer)));
    }

    public void annoncerFinPartie() {
        annoncer("finJeu");
    }

    public void annoncerDebutManche() {
        annoncer("debutManche " + gson.toJson(new DebutManche(jeu.getGrille())));
    }

    public void annoncerFinManche() {
        annoncer("finManche " + gson.toJson(new FinManche(jeu)));
    }

    public void annoncerMotTrouve(String nom) {
        annoncer("motTrouve " + gson.toJson(new MotTrouve(nom)));
    }

    /**
     * Annonce un message à tous les clients.
     *
     * @param message message à annoncer
     */
    private void annoncer(String message) {
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
            liste.add(l);
        }
        int valide = jeu.ajouterMotTrouve(
                liste,
                jeu.getJoueurs().stream()
                        .filter(j -> j.nom.equals(nouveauMot.getAuteur()))
                        .findFirst()
                        .get());
        if (valide > 0) {
            logger.info("Mot valide");
            annoncerMotTrouve(nouveauMot.getAuteur());
        } else {
            logger.info("Mot invalide");
        }
        client.envoyerMessage("motVerifie " + gson.toJson(new MotVerifie(nouveauMot.getId(), valide > 0, valide)));
    }

    /**
     * @param nouveauMot le nouveau mot clavier à ajouter
     * @param client le client qui a ajouté le mot
     */
    private void ajouterMot(NouveauMotClavier nouveauMot, Client client) {
        int valide = jeu.ajouterMotTrouve(
                nouveauMot.getMot(),
                jeu.getJoueurs().stream()
                        .filter(j -> j.nom.equals(nouveauMot.getAuteur()))
                        .findFirst()
                        .get());
        if (valide > 0) {
            logger.info("Mot valide");
            annoncerMotTrouve(nouveauMot.getAuteur());
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
                            chat.setAuteur(client.nom);
                            annoncerMessage(chat);
                            break;
                        case "status":
                            if (jeu.partieEstCommencee()) break;
                            Status status = gson.fromJson(donnees, Status.class);
                            status.setAuteur(client.nom);
                            annoncerStatus(status);

                            client.pret = true;
                            if (tousLesClientsSontPrets()) {
                                jeu.commencerPartie();
                                clients.forEach(c -> {
                                    Joueur j = new Joueur(c.nom);
                                    jeu.ajouterJoueur(j);
                                });
                            }
                            break;
                        case "motSouris":
                            NouveauMotSouris mot = gson.fromJson(donnees, NouveauMotSouris.class);
                            mot.setAuteur(client.nom);
                            ajouterMot(mot, client);
                            break;
                        case "motClavier":
                            NouveauMotClavier motClavier = gson.fromJson(donnees, NouveauMotClavier.class);
                            motClavier.setAuteur(client.nom);
                            ajouterMot(motClavier, client);
                            break;
                        case "stop":
                            exit = true;
                            break;
                        default:
                            logger.warn(motClef + " n'est pas reconnu");
                            break;
                    }
                    if (exit) {
                        logger.warn("Client \"" + client.nom + "\" s'est déconnecté");
                        annoncerDeconnextion(client);
                        client.arreter();
                        clients.remove(client);
                        break;
                    }
                } catch (IOException e) {
                    logger.warn("Perte de connexion du client \"" + client.nom + "\"");
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
    class Client {
        public final DataOutputStream dos;
        public final DataInputStream dis;
        public final Socket s;
        public final String nom;
        public boolean pret;

        private Client(Socket s, DataOutputStream dos, DataInputStream dis, String nom) {
            this.s = s;
            this.dos = dos;
            this.dis = dis;
            this.nom = nom;
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
        return clients.stream().allMatch(c -> c.pret);
    }

    public void stop() {
        try {
            clients.forEach(c -> c.arreter());
            serveur.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
