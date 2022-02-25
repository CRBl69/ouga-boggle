package com.boggle.client;

import com.boggle.serveur.jeu.ConfigurationClient;
import com.boggle.serveur.plateau.Mot;
import com.boggle.util.ConnexionServeurException;
import com.boggle.util.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/** La classe client qui communique avec le serveur */
public class Client {
    private Socket socket;
    private ConfigurationClient config;
    private Logger logger = Logger.getLogger("CLIENT");

    public Client(ConfigurationClient c) throws ConnexionServeurException {
        this.config = c;
        try {
            socket = new Socket(c.ip, c.port);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            if (!poigneeDeMain(dos, dis)) {
                throw new ConnexionServeurException("Mauvais mot de passe.");
            } else {
                GestionnaireServeur gestionnaireServeur = new GestionnaireServeur(dis);
                ClientTerminal clientTerminal = new ClientTerminal(dos);

                gestionnaireServeur.setClientTerminal(clientTerminal);
                clientTerminal.setGestionnaireServeur(gestionnaireServeur);

                gestionnaireServeur.start();
                clientTerminal.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ConnexionServeurException("Erreur de connexion au serveur, vérifiez l'IP et le port.");
        }
    }

    private boolean poigneeDeMain(DataOutputStream dos, DataInputStream dis) throws IOException {
        logger.info("Envoi du pseudo");
        dos.writeUTF(config.pseudo);
        logger.info("Pseudo envoyé");

        logger.info("Envoi du mot de passe");
        dos.writeUTF(config.mdp);
        logger.info("Mot de passe envoyé");

        String reponse = dis.readUTF();
        logger.info("Réponse : " + reponse);

        return reponse.equals("OK");
    }

    /** Permets de dire au serveur que le client est prêt. */
    public void pret() {}

    /** Permets de dire au serveur que le client n'est pas prêt. */
    public void pasPret() {}

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
}

class GestionnaireServeur extends Thread {
    private DataInputStream dis;
    private Logger logger = Logger.getLogger("CLIENT");
    private ClientTerminal clientTerminal;

    public GestionnaireServeur(DataInputStream dis) {
        this.dis = dis;
    }

    public void setClientTerminal(ClientTerminal clientTerminal) {
        this.clientTerminal = clientTerminal;
    }

    public void run() {
        try {
            while (true) {
                String message = dis.readUTF();
                logger.info("Message reçu : " + message);
            }
        } catch (IOException e) {
            logger.error("Connexion au serveur interrompue");
            this.clientTerminal.interrupt();
            System.exit(1);
        }
    }
}

class ClientTerminal extends Thread {
    private Scanner scanner = new Scanner(System.in);
    private DataOutputStream dos;
    private Logger logger = Logger.getLogger("CLIENT");
    private GestionnaireServeur gestionnaireServeur;

    public ClientTerminal(DataOutputStream dos) {
        super();
        this.dos = dos;
    }

    public void setGestionnaireServeur(GestionnaireServeur gestionnaireServeur) {
        this.gestionnaireServeur = gestionnaireServeur;
    }

    public void run() {
        try {
            while (true) {
                String msg = scanner.nextLine();
                dos.writeUTF(msg);
                if (msg.equals("stop")) {
                    break;
                }
            }
            logger.warn("Arrêt du client");
            gestionnaireServeur.interrupt();
        } catch (IOException e) {
            logger.error("Arrêt du client forcé");
            gestionnaireServeur.interrupt();
            System.exit(1);
        }
        System.exit(0);
    }
}
