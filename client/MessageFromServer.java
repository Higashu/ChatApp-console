package client;

import java.io.DataInputStream;
import java.net.Socket;

public class MessageFromServer extends Thread {

    Socket mUser;
    DataInputStream mReader;

    public MessageFromServer(Socket user) {
        super();
        this.mUser = user;
        try {
            mReader = new DataInputStream(mUser.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        String message = "";

        while (true) {
            try {
                message = mReader.readUTF();
                System.out.println(message);
            } catch (Exception e1) {
                // Le serveur est fermé
                System.out
                        .println("\n\nLa connexion a ete interrompue, appuyez sur entree pour quitter le programme\n");

                try {
                    mUser.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

                // Si on break pas la boucle tourne en continue sur l'erreur lorsque le serveur
                // ou le client se déconnecte
                break;
            }
        }

    }

}
