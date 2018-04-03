package fileclient;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import utilitaires.InterfaceTransport;
import utilitaires.Transport;

public class FileClient {
    private static final int PORT = 1234;

    public String runFileClient(float a, float b, String op) throws Exception {

        int exitCode = 0;
        InterfaceTransport t = null;

        // Initialise Socket
        try {
            t = new Transport("127.0.0.1", PORT);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Unable to connect to server");
            e.printStackTrace();
            System.exit(1);
        } // try

        InputStream in = null;

        try {
            t.envoyer(a + ":" + b + ":" + op);
            int ch;
            String serverStatus = t.recevoir().toString();

            if (serverStatus.startsWith("Bad")) {
                System.out.println("Bad");
                exitCode = 1;
                return "Bad";
            } else {
                System.out.println("Good");
                while ((ch = in.read()) >= 0) {
                    System.out.write((char) ch);
                    System.out.println(serverStatus);
                } // while
                return serverStatus;
                //System.out.println();
            } // if

        } catch (IOException e) {
            exitCode = 1;
        } finally {     // Ferme socket
            try {
                if (in != null)
                    in.close();
                t.fermer();
            } catch (IOException e) {
                exitCode = 1;
            } // try
        } // try

        //System.exit(exitCode);
        return "Exit code : " + exitCode;
    } // run
} // class FileClient
