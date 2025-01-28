package com.xulio.gal;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;

public class SSLServer {
    public static void main(String[] args) throws IOException {
        SSLSocket clientSocket = null;
        PrintWriter out=null;
        BufferedReader in=null;


        System.setProperty("javax.net.ssl.keyStore", "ServerKeys.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "12345678");

        int port = 60000;
        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory)
                SSLServerSocketFactory.getDefault();
        SSLServerSocket SSLserver = (SSLServerSocket)
                sslServerSocketFactory.createServerSocket(port);
        for (int i = 1; i < 5; i++) {
            System.out.println("Waiting for the client " + i);

            clientSocket = (SSLSocket) SSLserver.accept();
            out =new PrintWriter(clientSocket.getOutputStream(), true);
            in =new BufferedReader(new
                    InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Receiving from the client: " + i + " \n\t" +
                    in.readLine());

            out.println("Greetings to the client from the server");
        }

        in.close();
        out.close();
        clientSocket.close();
        SSLserver.close();
    }
}