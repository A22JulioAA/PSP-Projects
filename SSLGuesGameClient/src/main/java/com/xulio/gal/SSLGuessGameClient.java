package com.xulio.gal;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SSLGuessGameClient {
    public static void main(String[] args) {

        System.setProperty("javax.net.ssl.trustStore", "C:\\ClientKeys.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "87654321");

        try {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket("localhost", 60000);

            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            var sc = new Scanner(System.in);

            while (true) {
                System.out.println(in.nextLine());
                String command = sc.nextLine();

                out.println(command);
            }

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}