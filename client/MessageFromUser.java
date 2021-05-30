package client;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.Socket;

public class MessageFromUser extends Thread {

    Socket mUser;
    DataOutputStream mSender;
    BufferedReader mSaisie;

    public MessageFromUser(Socket user) {
        super();
        this.mUser = user;
        try {
            mSaisie = new BufferedReader(new InputStreamReader(System.in));
            mSender = new DataOutputStream(mUser.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message = "";

        while (true) {
            try {
                message = mSaisie.readLine();
                mSender.writeUTF(message);
                mSender.flush();
            } catch (Exception e) {
                break;
            }
        }

    }

}
