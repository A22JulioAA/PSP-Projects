package com.xulio.gal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SSLGuessGameServer {
    public static void main(String[] args) {
        int PORT = 60000;

        try (ServerSocket listener = new ServerSocket(PORT)) {
            System.out.println("The server is running in port " + PORT + "...");
            System.out.println("Waiting for players!");

            while (true) {
                try {
                    Socket socket = listener.accept();

                    System.out.println("Client connected to server with IP: " + socket.getInetAddress());

                    Thread serverTh = new Thread(new GuessGameClientHandler(socket));

                    serverTh.start();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}