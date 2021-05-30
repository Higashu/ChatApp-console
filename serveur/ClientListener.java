package serveur;

import java.io.DataInputStream;
import java.net.Socket;

//thread créé par la classe User, permet d'écouter les entrées du client
public class ClientListener extends Thread {

    private Socket mSocket;
    private DataInputStream mInputStream;
    private String mUserName;
    private String messageEntrant = "";

    // Constructeur de ClientListener
    public ClientListener(Socket socket, String username) {

        super();

        mSocket = socket;
        mUserName = username;

        try {

            mInputStream = new DataInputStream(mSocket.getInputStream());

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // Permet de redéfinir le méthode run appeler avec la méthode start() de la
    // classe Thread
    @Override
    public void run() {

        while (!messageEntrant.equals("exit")) {

            try {

                messageEntrant = mInputStream.readUTF();
                Serveur.getInstance().dispatchMessage(messageEntrant, mUserName);

            } catch (Exception e) {
                // Le client s'est déconnecté salement
                break;

            }
        }

        Serveur.getInstance().disconnectClient(mUserName);
        Serveur.getInstance().dispatchMessage(mUserName.concat(" a quitte la conversation."), null);

        try {

            mSocket.close();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}