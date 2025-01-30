package com.xulio.gal;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.Socket;

public class SSLGuessGameServer {
    public static void main(String[] args) {
        int PORT = 60000;

        System.setProperty("javax.net.ssl.keyStore", "C:\\ServerKeys.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "12345678");

        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        try (SSLServerSocket listener = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT)) {
            System.out.println("The server is running in port " + PORT + "...");
            System.out.println("Waiting for players!");

            // Set the allowed server protocols.
            listener.setEnabledProtocols(new String[] {"TLSv1.2", "TLSv1.3"});

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