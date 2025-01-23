package com.xulio.gal;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PowServer {
    public static void main(String[] args) throws IOException {
        try (ServerSocket listener = new ServerSocket(57777)) {
            System.out.println("The server is running...");

            while (true) {
                try {
                    Socket socket = listener.accept();

                    System.out.println("Client connected to server with IP: " + socket.getInetAddress());

                    Thread serverTh = new Thread(new PowClientHandler(socket));

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
