package com.xulio.gal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.Scanner;

public class RandomWordClient {
    public static void main(String[] args) {
        final int SERVER_PORT = 60000;
        byte[] buffer = new byte[1024];

        try {
            InetAddress serverAddress = InetAddress.getByName("localhost");

            DatagramSocket socketUDP = new DatagramSocket();

            Scanner scanner = new Scanner(System.in);
            String command = "";
            String regex = "^WORD \\d+$";

            while (true) {
                System.out.println("Enter a command: ");
                command = scanner.nextLine();

                if (command.equalsIgnoreCase("QUIT")) {
                    System.out.println("Exiting client...");
                    break;
                }

                if (!command.matches(regex)) {
                    System.out.println("Invalid command. Please enter 'WORD <num_letters>'");
                    continue;
                }

                buffer = command.getBytes("UTF-8");

                DatagramPacket question = new DatagramPacket(buffer, buffer.length, serverAddress, SERVER_PORT);

                System.out.println("Sending command to server...");
                socketUDP.send(question);

                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                socketUDP.receive(response);
                System.out.println("Receiving the response");

                String serverResponse = new String(response.getData(), response.getOffset(), response.getLength(), "UTF-8");
                System.out.println("Server response: " + response);
            }

            socketUDP.close();
            scanner.close();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}