package serveur;

import java.io.DataOutputStream;
import java.net.Socket;

//Cette classe représente un client qui s'est connecté sur notre serveur
public class User {

    private Socket mSock;
    private String mUserName;
    private DataOutputStream mSender;
    private Thread mListener;

    // Envoie le message vers le client
    public void sendMessage(String message) {

        try {

            mSender.writeUTF(message);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    // Renvoie le pseudo de l'utilisateur
    public String getUserName() {

        return mUserName;

    }

    // Constructeur
    public User(Socket sock, String UserName) {

        mSock = sock;
        mUserName = UserName;

        try {

            mSender = new DataOutputStream(mSock.getOutputStream());

        } catch (Exception e) {

            e.printStackTrace();

        }

        mListener = new ClientListener(sock, mUserName);
        mListener.start();

    }

}