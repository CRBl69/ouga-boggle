package com.boggle.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.boggle.serveur.jeu.ConfigurationClient;
import com.boggle.util.ConnexionServeurException;
import com.boggle.util.Mot;

/** La classe client qui communique avec le serveur */
public class Client {
    private Scanner scanner = new Scanner(System.in);
    private Socket socket;

    public Client(ConfigurationClient c) throws ConnexionServeurException {
        try {
            socket = new Socket(c.ip, c.port);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            if(!poigneeDeMain(dos, dis)) {
                throw new ConnexionServeurException("Mauvais mot de passe.");
            } else {
                Thread serverHandler = new ServerHandler(dis);
                serverHandler.start();
                while (true) {
                    String tosend = scanner.nextLine();
                    dos.writeUTF(tosend);
                    if(tosend.equals("Exit"))
                    {
                        System.out.println("Closing this connection : " + socket);
                        serverHandler.interrupt();
                        socket.close();
                        System.out.println("Connection closed");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new ConnexionServeurException("Erreur de connexion au serveur, vérifiez l'IP et le port.");
        }

    }

    private boolean poigneeDeMain(DataOutputStream dos, DataInputStream dis) throws IOException {
        System.out.print("Nom d'utilisateur: ");
        dos.writeUTF(scanner.nextLine());
        System.out.print("\nMot de passe: ");
        dos.writeUTF(scanner.nextLine());
        String response = dis.readUTF();

        return response.equals("OK");
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

class ServerHandler extends Thread {
    private DataInputStream dis;

    public ServerHandler(DataInputStream dis) {
        this.dis = dis;
    }

    public void run() {
        try {
            while (true) {
                String message = dis.readUTF();
                System.out.println(message);
            }
        } catch (IOException e) {
        }
    }
}
