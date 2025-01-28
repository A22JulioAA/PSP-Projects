package com.xulio.gal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GuessGameClientHandler implements Runnable {
    private Socket clientSocket;

    public GuessGameClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("Welcome!");
            out.println("Introduce your commands to play! If you don't know how game works, enter 'HELP'");

            String line;

            while ((line = in.readLine()) != null) {
                System.out.println("Client " + clientSocket.getInetAddress() + " introduce: " + line);

                switch (line) {
                    case "NEW":
                        
                        break;
                    case "NUM":
                    case "HELP":
                    case "QUIT":

                }
                out.println("Command: " + line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
