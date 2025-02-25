package com.xulio.gal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FundsRaisingClientHandler implements Runnable {
    private Socket clientSocket;
    private final Funds funds;
    private static final Logger logger = Logger.getLogger(FundsRaisingClientHandler.class.getName());

    public FundsRaisingClientHandler(Socket clientSocket, Funds funds) {
        this.clientSocket = clientSocket;
        this.funds = funds;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {
            out.println("Connected!");

            String command;
            while ((command = in.readLine()) != null) {
                String[] parts = command.trim().split("\\s+");
                String action = parts[0].toUpperCase();

                switch (action) {
                    case "ADD":
                        if (parts.length == 2) {
                            try {
                                int amount = Integer.parseInt(parts[1]);
                                addFunds(out, amount);
                            } catch (NumberFormatException e) {
                                out.println("Invalid amount. Use ADD <number> ");
                            }
                        } else {
                            out.println("Invalid amount. Use ADD <number> ");
                        }
                        break;
                    case "SHOW":
                        showTotalFunds(out);
                        break;
                    case "QUIT":
                        quitServer(out, clientSocket);
                        return;
                    default:
                        out.println("Invalid command!");
                        break;
                }
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error handling client: " + clientSocket.getInetAddress(), e);
        } finally {
            closeSocket();
        }
    }

    private void showTotalFunds (PrintWriter out) {
        int totalFunds = funds.getTotalFunds();
        out.println(totalFunds > 0 ? "Total funds: " + totalFunds : "No founds found...");
    }

    private void addFunds (PrintWriter out, int amount) {
        if (amount <= 0) {
            out.println("Amount must be greater than 0.");
            return;
        }

        funds.addFunds(amount);

        out.println("Added " + amount + ". New total: " + funds.getTotalFunds());

        System.out.println(clientSocket.getInetAddress() + "(" + clientSocket.getPort() + ") added " + amount + "€!");
    }

    private void quitServer (PrintWriter out, Socket clientSocket) {
        out.println("Goodbye!");
        System.out.println("Client " + clientSocket.getInetAddress() + "(" + clientSocket.getPort() + ") disconnected...");
    }

    private void closeSocket () {
        try {
            clientSocket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing socket: " + clientSocket.getInetAddress(), e);
        }
    }
}
