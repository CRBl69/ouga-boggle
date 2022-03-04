package com.boggle.client;

import com.boggle.serveur.jeu.ConfigurationClient;
import com.boggle.serveur.messages.*;
import com.boggle.serveur.plateau.Mot;
import com.boggle.util.ConnexionServeurException;
import com.boggle.util.Logger;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

/** La classe client qui communique avec le serveur */
public class Client {
    private Socket socket;
    private ConfigurationClient config;
    private Logger logger = Logger.getLogger("CLIENT");
    private DataInputStream dis;
    private DataOutputStream dos;
    private int nClients;
    private int nPrets;
    private AffichageStatus affichageStatus;
    private AffichageJeu affichageJeu;
    private Gson gson = new Gson();
    private HashMap<String, String> motsEnVerification = new HashMap<>();

    public Client(ConfigurationClient c) throws ConnexionServeurException {
        this.config = c;
        try {
            socket = new Socket(c.ip, c.port);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            if (!poigneeDeMain(dos, dis)) {
                throw new ConnexionServeurException("Mauvais mot de passe.");
            } else {
                GestionnaireServeur gestionnaireServeur = new GestionnaireServeur(dis);

                gestionnaireServeur.start();

                affichageStatus = new AffichageStatus(this);
                affichageStatus.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnexionServeurException("Erreur de connexion au serveur, vérifiez l'IP et le port.");
        }
    }

    /**
     * Effectue la poignée de main avec le serveur.
     *
     * Elle envoie d'abord le pseudo, puis le mot de passe. Si le mot de passe
     * est correct, la fonction extrait le nombre de clients déjà connectés et le
     * nombre de clients prêts de la réponse serveur.
     *
     * @param message le message à envoyer
     * @throws IOException
     */
    private boolean poigneeDeMain(DataOutputStream dos, DataInputStream dis) throws IOException {
        logger.info("Envoi du pseudo");
        dos.writeUTF(config.pseudo);
        logger.info("Pseudo envoyé");

        logger.info("Envoi du mot de passe");
        dos.writeUTF(config.mdp);
        logger.info("Mot de passe envoyé");

        String reponse = dis.readUTF();
        logger.info("Réponse : " + reponse);

        if (reponse.equals("NOPE")) return false;

        String[] s = reponse.split(" ");
        nPrets = Integer.parseInt(s[1]);
        nClients = Integer.parseInt(s[2]);

        return true;
    }

    /** Permets de dire au serveur que le client est prêt ou pas. */
    public void envoyerStatus(boolean status) {
        try {
            dos.writeUTF(String.format("status {\"status\": %b}", status));
        } catch (IOException e) {
        }
    }

    /**
     * Permets d'envoyer un mot au serveur.
     * @param mot Le mot à envoyer.
     */
    public void mot(Mot mot) {}

    /**
     * Permets d'envoyer un message au serveur.
     * @param message Le message à envoyer.
     */
    public void message(String message) {}

    public int getNClients() {
        return nClients;
    }

    public int getNPrets() {
        return nPrets;
    }

    /** Classe interne qui gère les messages envoyés par le serveur. */
    class GestionnaireServeur extends Thread {
        private DataInputStream dis;
        private Logger logger = Logger.getLogger("CLIENT");

        public GestionnaireServeur(DataInputStream dis) {
            this.dis = dis;
        }

        public void run() {
            try {
                while (true) {
                    String message = dis.readUTF();
                    logger.info("Message reçu : " + message);
                    String motClef = message.split(" ")[0];
                    String donnees = "";
                    if (!motClef.equals(message)) donnees = message.substring(motClef.length() + 1);

                    switch (motClef) {
                        case "message":
                            Chat chat = gson.fromJson(donnees, Chat.class);
                            affichageJeu.ajouterChat(chat.toString());
                            break;
                        case "status":
                            Status status = gson.fromJson(donnees, Status.class);
                            if (status.getStatus()) nPrets++;
                            else nPrets--;
                            affichageStatus.update();
                            break;
                        case "motVerifie":
                            MotVerifie mot = gson.fromJson(donnees, MotVerifie.class);
                            mot.setMot(motsEnVerification.get(mot.getId()));
                            affichageJeu.ajouterMotVerifie(mot);
                            break;
                        case "stop":
                            break;
                        case "connexion":
                            nClients++;
                            affichageStatus.update();
                            break;
                        case "deconnexion":
                            nClients--;
                            affichageStatus.update();
                            break;
                        case "motTrouve":
                            MotTrouve motTrouve = gson.fromJson(donnees, MotTrouve.class);
                            if (!motTrouve.getNom().equals(config.pseudo)) {
                                affichageJeu.ajouterMotTrouve(motTrouve);
                            }
                            break;
                        case "debutJeu":
                            affichageStatus.setVisible(false);
                            affichageJeu = new AffichageJeu(new Serveur(dos));
                            break;
                        case "debutManche":
                            DebutManche debutManche = gson.fromJson(donnees, DebutManche.class);
                            affichageJeu.ajouterChat("Début de la prochaine manche.");
                            affichageJeu.initManche(debutManche.getTableau());
                            break;
                        case "finManche":
                            FinManche finManche = gson.fromJson(donnees, FinManche.class);
                            affichageJeu.finManche(finManche);
                            break;
                        default:
                            logger.warn(message + " n'est pas reconnu");
                            break;
                    }
                }
            } catch (IOException e) {
                logger.error("Connexion au serveur interrompue");
                System.exit(1);
            }
        }
    }

    /** Classe permettant d'envoyer des messages au serveur. */
    class Serveur {
        private DataOutputStream dos;
        private Logger logger = Logger.getLogger("CLIENT");

        public Serveur(DataOutputStream dos) {
            super();
            this.dos = dos;
        }

        private void envoyer(String msg) {
            try {
                dos.writeUTF(msg);
            } catch (IOException e) {
                logger.error("Erreur d'envoi de message au serveur");
            }
        }

        /**
         * Permets d'envoyer un mot au serveur.
         *
         * @param mot Le mot à envoyer.
         */
        public void envoyerMotClavier(String mot) {
            var motObj = new NouveauMotClavier(mot, UUID.randomUUID().toString());
            motsEnVerification.put(motObj.getId(), mot);
            envoyer("motClavier " + gson.toJson(motObj));
        }
    }
}
