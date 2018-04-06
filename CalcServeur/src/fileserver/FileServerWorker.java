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

        } catch (IOException e) {
            activeConnectionCount--;
            if (out != null)
                try {
                    t.envoyer("Bad:" + this.result + "\n");
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
        }


        try {
            String[] data = fileName.split(":");
            this.result = interfaceModel.calculer(Float.valueOf(data[0]), Float.valueOf(data[1]), data[2]);
            System.out.println(this.result);
            if (data[2].equals("+") || data[2].equals("-") || data[2].equals("*") || data[2].equals("/") || data[2].equals("^")) {
                t.envoyer(this.result);
            } else {
                t.envoyer("Opération non définis");
            }

        } catch (DivisionException d) {
            System.out.println("Division par O interdite...");
            try {
                t.envoyer("Division par O interdite");
            } catch (IOException ex) {
                Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (OpException o) {
            System.out.println("Opération non définis...");
            try {
                t.envoyer("Opération non définis");
            } catch (IOException ex) {
                Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileServer.class.getName()).log(Level.SEVERE, null, ex);
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