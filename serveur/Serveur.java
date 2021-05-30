package serveur;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.net.SocketTimeoutException;

public class Serveur {

    // Tableau qui repertorie tout les clients connectés
    private ArrayList<User> arrayOfClient = new ArrayList<User>();

    // Tableau qui repertorie tout les pseudos des clients
    private ArrayList<String> arrayOfUsername = new ArrayList<String>();

    // Constructeur privé car Serveur est un singleton
    private Serveur() {
    }

    // Instance du singleton
    private static Serveur singleton = new Serveur();

    // Méthode pour obtenir l'unique instance de la classe Serveur
    public static Serveur getInstance() {

        return singleton;

    }

    // Log les messages entrant sur la sortie standard du serveur
    private void logMessage(String message, String sender) {

        if (sender == null)
            System.out.println(message);
        else
            System.out.println(sender.concat(" a ecrit : ".concat(message)));

    }

    // Appelle la méthode sendMessage de la classe User pour chaque objet User
    // contenu dans le tableau arrayOfClient
    public void dispatchMessage(String message, String sender) {

        logMessage(message, sender);

        if (sender == null) {

            for (User element : arrayOfClient)
                element.sendMessage(message);

        } else {

            for (User element : arrayOfClient)
                element.sendMessage(sender.concat(" a dit : ".concat(message)));

        }

    }

    // Execute une routine lorsqu'un client se déconnecte
    // Supprime l'objet User qui represente le client connecté et le nom
    // d'utilisateur du même client
    public void disconnectClient(String clientDisconnected) {

        arrayOfUsername.remove(clientDisconnected);
        arrayOfClient.removeIf(element -> (element.getUserName() == clientDisconnected));

    }

    // Dit si un nom d'utilisateur est déjà utilisé ou non
    private boolean usernameAlreadyUsed(String userName) {

        return arrayOfUsername.contains(userName);

    }

    // Fonction qui ajoute un nouveau client dans arrayOfClient et le pseudo associé
    // a arrayOfUsername
    private void addClient(Socket sock, User muser) {

        arrayOfUsername.add(muser.getUserName());
        arrayOfClient.add(muser);

    }

    // Dit si il y a encore des clients connectés
    public boolean thereIsClient() {

        return !arrayOfClient.isEmpty();

    }

    // Boucle infini pour accepter les connexion entrante, se ferme lorsqu'il n'y a
    // plus d'utilisateur connecté
    public static void main(String[] args) {

        Socket sockClient;
        DataInputStream inputStream;
        DataOutputStream outputStream;
        String userName;
        User muser;

        try {

            ServerSocket serveur = new ServerSocket(4242);
            serveur.setSoTimeout(60000);

            do {

                try {
                    sockClient = serveur.accept();

                    System.out.println("Nouvelle connexion !");
                    outputStream = new DataOutputStream(sockClient.getOutputStream());
                    outputStream.writeUTF("Entrez votre pseudo : ");
                    inputStream = new DataInputStream(sockClient.getInputStream());
                    userName = inputStream.readUTF();

                    if (Serveur.getInstance().usernameAlreadyUsed(userName)) {

                        while (Serveur.getInstance().usernameAlreadyUsed(userName)) {

                            outputStream.writeUTF("Pseudo deja existant, entrez un autre pseudo : ");
                            userName = inputStream.readUTF();

                        }

                    }

                    muser = new User(sockClient, userName);

                    Serveur.getInstance().addClient(sockClient, muser);

                    Serveur.getInstance().dispatchMessage(userName.concat(" a rejoint la conversation."), null);

                    muser.sendMessage("-------------------------------------------------");

                    // Le temps pour recevoir une connexion est dépassé
                } catch (SocketTimeoutException err) {

                    if (!Serveur.getInstance().thereIsClient()) {
                        System.out.println("Plus aucun client connecte, fermeture du serveur.");
                        break;
                    }
                }

            } while (true);

            serveur.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
