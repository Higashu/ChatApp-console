package client;

import java.net.Socket;

public class Client {

    public static void main(String[] args) {

        MessageFromUser mSender;
        MessageFromServer mReceiver;

        try {
            Socket connexion = new Socket("localhost", 4242);

            mReceiver = new MessageFromServer(connexion);
            mSender = new MessageFromUser(connexion);

            mReceiver.start();
            mSender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // verif username

}