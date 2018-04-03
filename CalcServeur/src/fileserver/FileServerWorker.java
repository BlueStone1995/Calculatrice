package fileserver;

import model.Exception.DivisionException;
import model.Exception.OpException;
import model.InterfaceModel;
import utilitaires.InterfaceTransport;
import utilitaires.Transport;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileServerWorker implements Runnable {

    private InterfaceTransport t;
    private int activeConnectionCount = 0;
    private boolean shutDownFlag = false;
    private InterfaceModel interfaceModel;
    private float result;

    FileServerWorker(Socket s, InterfaceModel interfaceModel) throws IOException {
        this.t = new Transport(s);
        new Thread(this).start();

        this.interfaceModel = interfaceModel;
    } // constructor(Socket)

    public void run() { // Lance nouveau thread

        Object in = null;
        String fileName = "";
        Object out = null;
        FileInputStream f = null;

        activeConnectionCount++;
        System.out.println("Lancement du thread pour gérer" +
                " le protocole avec un client");

        // read the file name sent by the client and open the file.
        try {
            fileName = t.recevoir().toString();
            System.out.println(fileName);

            String[] data = fileName.split(":");
            this.result = interfaceModel.calculer(Float.valueOf(data[0]), Float.valueOf(data[1]), data[2]);
            System.out.println(this.result);

            f = new FileInputStream(fileName);

        } catch (IOException e) {
            activeConnectionCount--;
            if (out != null)
                try {
                    t.envoyer("Bad:" + fileName + "\n");
                } catch (IOException ex) {
                    Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            try {
                t.fermer();
            } catch (IOException ie) {
            }
            return;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DivisionException d) {
            System.out.println("Division par O interdite...");
        } catch (OpException o) {
            System.out.println("Opération non définis...");
        }// try

        try {
            // send contents of file to client.
            t.envoyer("Good:\n");
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
        }

        byte[] buffer = new byte[4096];

        try {
            int len;
            while (!shutDownFlag && (len = f.read(buffer)) > 0) {
                String reponse = new String(buffer, 0, len);
                //t.envoyer(buffer + "OK" + len);
                t.envoyer(reponse);
            } // while
        } catch (IOException e) {
        } finally {
            try {
                activeConnectionCount--;
                t.fermer();
            } catch (IOException e) {
            } // try
        } // try
    } // run()

    public void setShutDownFlag(boolean shutDownFlag) {
        this.shutDownFlag = shutDownFlag;
    }

} // class FileServerWorker