package fileserver;

import fileserver.FileServer;
import model.*;
import model.Exception.OpException;


public class MainServer {

    static Calculator calculator;

    public static void main(String[] arg) {

        FileServer fs = new FileServer();
        fs.runServer();

    }

} 