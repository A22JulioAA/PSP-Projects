package com.xulio.gal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RandomWordServer {
    public static final int MAX_CONNECTIONS = 10;
    protected static final AtomicInteger activeConnections = new AtomicInteger(0);
    private static final int PORT = 60000;
    private static byte[] buffer = new byte[1024];

    private static Logger logger = Logger.getLogger(RandomWordServer.class.getName());

    public static void main(String[] args) {
        try {
            System.out.println("UDP server started. Waiting for clients...");

            DatagramSocket socketUDP = new DatagramSocket(PORT);

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);

                socketUDP.receive(request);

                String message = new String(request.getData(), 0, request.getLength(), "UTF-8");

                System.out.println(message);

                int clientPort = request.getPort();

                InetAddress address = request.getAddress();

                message = "Hello World from the server!";
                buffer = message.getBytes();

                DatagramPacket answer = new DatagramPacket(buffer, buffer.length, address, clientPort);

                System.out.println("Sending information to the client...");

                socketUDP.send(answer);
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}