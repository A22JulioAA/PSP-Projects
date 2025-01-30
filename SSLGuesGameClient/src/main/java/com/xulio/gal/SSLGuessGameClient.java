package com.xulio.gal;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSLGuessGameClient {
    private static final String SERVERNAME = "localhost";
    private static final int PORT = 60000;

    private static Logger logger = Logger.getLogger(SSLGuessGameClient.class.getName());

    public static void main(String[] args) {

        System.setProperty("javax.net.ssl.trustStore", "C:\\ClientKeys.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "87654321");

        try (
                SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(SERVERNAME, PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                var sc = new Scanner(System.in);
        ) {
            String serverResponse;
            while ((serverResponse = in.readLine()) != null) {
                System.out.println(serverResponse);

                if (serverResponse.equalsIgnoreCase("11 BYE")) {
                    System.out.println("Server has closed the connection. Exiting...");
                    break;
                }

                String command = sc.nextLine();
                out.println(command);
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}