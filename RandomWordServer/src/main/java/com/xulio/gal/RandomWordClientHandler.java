package com.xulio.gal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RandomWordClientHandler implements Runnable {
    private Socket clientSocket;

    private static Logger logger = Logger.getLogger(RandomWordClientHandler.class.getName());

    public RandomWordClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {

        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
