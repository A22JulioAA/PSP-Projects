package com.xulio.gal;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSLGuessGameServer {
    private static final int MAX_CONNECTIONS = 10;
    protected static final AtomicInteger activeConnections = new AtomicInteger(0);
    private static final int PORT = 60000;

    private static Logger logger = Logger.getLogger(SSLGuessGameServer.class.getName());

    public static void main(String[] args) {
        System.setProperty("javax.net.ssl.keyStore", "keys/ServerKeys.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "12345678");

        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        try (SSLServerSocket listener = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT)) {
            // Display server running in server console.
            System.out.println("The server is running in port " + PORT + "...");
            System.out.println("Waiting for players!");

            // Set the allowed server protocols.
            listener.setEnabledProtocols(new String[] {"TLSv1.2", "TLSv1.3"});

            // Accept connections with a infinite loop.
            while (true) {
                if (activeConnections.get() < MAX_CONNECTIONS) {
                    try {
                        Socket socket = listener.accept();

                        activeConnections.incrementAndGet();

                        System.out.println("Client connected to server with IP: " + socket.getInetAddress());

                        Thread serverTh = new Thread(new GuessGameClientHandler(socket));
                        serverTh.start();
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, e.getMessage());
                    }
                } else {
                    logger.log(Level.INFO, "Maximum connections reached. Waiting for a slot...");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}