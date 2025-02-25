package com.xulio.gal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FundsRaisingClient {
    private static final int PORT = 60000;
    private static final String HOST = "localhost";
    private static final Logger logger = Logger.getLogger(FundsRaisingClient.class.getName());

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in);
        ) {
            System.out.println(in.readLine());

            String userInput;
            while (true) {
                System.out.print("> ");
                userInput = scanner.nextLine().trim();

                if (userInput.isEmpty()) continue;

                out.println(userInput);

                String response = in.readLine();
                if (response == null) break;

                System.out.println(response);

                if ("Goodbye!".equalsIgnoreCase(response)) {
                    break;
                }
            }

        } catch (UnknownHostException e) {
            logger.log(Level.SEVERE, "Unknown host: " + HOST, e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error connecting to the server...", e);
        }
    }
}
