package com.boggle.serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.boggle.serveur.jeu.ConfigurationServeur;
import com.boggle.serveur.jeu.Jeu;
import com.boggle.serveur.messages.*;
import com.boggle.serveur.plateau.Grille;
import com.boggle.serveur.plateau.Lettre;
import com.boggle.util.Logger;

/** Gère la communication avec tous les clients. */
public class Serveur {
    private Logger logger = Logger.getLogger("SERVEUR");
    private ServerSocket serveur;
    private ArrayList<Client> clients = new ArrayList<>();
    private String password;
    private Gson gson = new Gson();
    private Jeu jeu;

    public Serveur(ConfigurationServeur c) throws IOException {
        this.password = c.mdp;
        serveur = new ServerSocket(c.port);
        jeu = new Jeu(c.nbManches, new Grille(c.tailleGrilleV, c.tailleGrilleV, c.langue));
        demarerServeur();
    }

    public void demarerServeur() {
        while (true) {
            Socket s = null;
            try {
                s = serveur.accept();
                logger.info("Nouveau client connecté");

                Client client = poigneeDeMain(s);
                if(client != null) {
                    clients.add(client);
                    Thread t = new ClientHandler(client);
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

    private Client poigneeDeMain(Socket s) throws IOException {
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // On demande un nom et un mot de passe au client
        String name = dis.readUTF();
        String password = dis.readUTF();

        if(password.equals(this.password)) {
            dos.writeUTF("OK");
            return new Client(s, dos, dis, name);
        } else {
            dos.writeUTF("NOPE");
            dos.close();
            dis.close();
            s.close();
            return null;
        }
    }

    private void message(Chat message) {
        for (Client c : clients) {
            c.envoyerMessage("message " + gson.toJson(message));
        }
    }

    private void ajouterMot(NouveauMot nouveauMot) {
        try {
            LinkedList<Lettre> liste = new LinkedList<>();
            for (Lettre l : nouveauMot.getLettres()) {
                liste.add(l);
            }
            logger.info("Mot valide");
            //jeu.ajouterMotTrouve(m);
        } catch(IllegalArgumentException e) {
            logger.error("Mot invalide");
        }
    }

    private class ClientHandler extends Thread {
        private Client client;

        public ClientHandler(Client c) {
            this.client = c;
        }

        public void run() {
            String line = "";
            boolean exit = false;
            while(true) {
                try {
                    line = client.dis.readUTF();
                    logger.info("Message reçu : " + line);
                    String motClef = line.split(" ")[0];
                    String data = line.substring(motClef.length() + 1);
                    switch (motClef) {
                        case "message":
                            Chat chat = gson.fromJson(data, Chat.class);
                            chat.setAuteur(client.name);
                            message(chat);
                            break;
                        case "pret":
                            break;
                        case "pasPret":
                            break;
                        case "mot":
                            NouveauMot mot = gson.fromJson(data, NouveauMot.class);
                            ajouterMot(mot);
                            break;
                        case "exit":
                            exit = true;
                            break;
                        default:
                            logger.warn(motClef + " n'est pas reconnu");
                            break;
                    }
                    if(exit) {
                        client.arreter();
                        break;
                    }
                } catch (IOException e) {
                    logger.warn("Client déconnecté");
                    break;
                }
            }
        }
    }

    class Client {
        public final DataOutputStream dos;
        public final DataInputStream dis;
        public final Socket s;
        public final String name;

        private Client(Socket s, DataOutputStream dos, DataInputStream dis, String name) {
            this.s = s;
            this.dos = dos;
            this.dis = dis;
            this.name = name;
        }

        public void envoyerMessage(String s) {
            try {
                this.dos.writeUTF(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
}
