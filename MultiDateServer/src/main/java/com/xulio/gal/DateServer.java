package com.xulio.gal;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class DateServer {
    public static void main(String[] args) {
        try (ServerSocket listener = new ServerSocket(57778)) {
            System.out.println("The date server is running...");
            while (true) {
                try {
                    Socket socket = listener.accept();
                    System.out.println("Client connected to server with IP " + socket.getInetAddress());
                    Thread serverTh = new Thread(new DateServerWorker(socket));

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
