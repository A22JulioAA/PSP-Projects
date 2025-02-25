package com.xulio.gal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FundsRaisingServer {
    private static final int PORT = 60000;
    private static Logger logger = Logger.getLogger(FundsRaisingServer.class.getName());

    public static void main(String[] args) {
        Funds sharedFunds = new Funds();

        try (ServerSocket listener = new ServerSocket(PORT)) {

            System.out.println("Server running in port " + PORT + "...");

            while (true) {
                try {
                    Socket socket = listener.accept();

                    System.out.println("Client connected to server (" + socket.getInetAddress() + ")");

                    Thread serverTh = new Thread(new FundsRaisingClientHandler(socket, sharedFunds));
                    serverTh.start();
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}