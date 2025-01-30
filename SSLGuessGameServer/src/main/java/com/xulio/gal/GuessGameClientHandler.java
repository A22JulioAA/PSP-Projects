package com.xulio.gal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GuessGameClientHandler implements Runnable {
    private Socket clientSocket;
    private NumberGuessGame numberGuessGame;

    private static Logger logger = Logger.getLogger(GuessGameClientHandler.class.getName());

    public GuessGameClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {

            out.println(ServerResponses.SERVER_READY.getResponse());
            //showMainMenu(out);

            String command;

            while ((command = in.readLine()) != null) {
                switch (command.toUpperCase()) {
                    case "NEW":
                        startNewGame(in, out, 6);
                        break;
                    case "HELP":
                        showHelpMessage(out);
                        break;
                    case "QUIT":
                        exitServer(out, clientSocket);
                        return;
                    default:
                        if (command.toUpperCase().startsWith("NEW ")) {
                            handleNewCommandWithTries(in, out, command);
                        } else {
                            setErrorOnClient(out, command);
                        }
                        break;
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        } finally {
            try {
                SSLGuessGameServer.activeConnections.decrementAndGet();
                clientSocket.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
    }

    // Function to show HELP message.
    protected void showHelpMessage(PrintWriter out) {
        String helpMessage =
                "--------------- HELP ---------------" +
                        "Available commands:" +
                        "NEW  -> Start a new game. It accepts a parameter, the number of tries you want to guess the number." +
                        "        Example: NEW 8" +
                        "NUM  -> The guess number. A game must be created before this command." +
                        "        Example: NUM 44" +
                        "HELP -> Show help information." +
                        "QUIT -> Exit the program." +
                        "------------------------------------";

        out.println(helpMessage);
    }

    // Function to start a new game.
    protected void startNewGame (BufferedReader in, PrintWriter out, int tries) throws IOException {
        this.numberGuessGame = new NumberGuessGame();

        System.out.println("Guess number to player " + clientSocket.getInetAddress() +
                " is: " + numberGuessGame.getNumberToGuess());

        out.println(ServerResponses.PLAY.getResponse() + " < " + tries + " >");

        while (!numberGuessGame.isGuessed()) {
            if (tries >= 0) {
                String line = in.readLine();

                if (line == null || line.trim().isEmpty()) {
                    continue;
                }

                if (line != null && line.equalsIgnoreCase("QUIT")) {
                    exitServer(out, clientSocket);

                    return;
                }

                if (line != null && line.toUpperCase().startsWith("NEW")) {
                    out.println(ServerResponses.ERR.getResponse());

                    continue;
                }

                if (line != null && line.toUpperCase().startsWith("NUM ")) {
                    String[] parts = line.split(" ");

                    if (parts.length == 2) {
                        try {
                            int guess = Integer.parseInt(parts[1]);
                            String response = numberGuessGame.guess(guess, tries);
                            out.println(response);
                        } catch (NumberFormatException e) {
                            out.println(ServerResponses.SERVER_READY_BEFORE_ERROR.getResponse());
                        }
                    }
                } else {
                    out.println(ServerResponses.SERVER_READY_BEFORE_ERROR.getResponse());
                }

                tries--;
            } else {
                out.println(ServerResponses.LOSE_NUM.getResponse() +
                        " < " + numberGuessGame.getNumberToGuess() + " >");
                break;
            }

        }
    }

    // Function to start a new game with X tries.
    protected void handleNewCommandWithTries (BufferedReader in, PrintWriter out, String command) {
        String[] parts = command.split(" ");

        if (parts.length == 2) {
            try {
                int tries = Integer.parseInt(parts[1]);

                if (tries > 0) {
                    startNewGame(in, out, tries);
                } else {
                    out.println("Number of tries must be greater than 0.");
                }
            } catch (NumberFormatException | IOException e) {
                out.println("Invalid number of tries. Please enter a valid number");
            }
        } else {
            out.println(ServerResponses.UNKNOWN.getResponse());
        }
    }

    // Function to exit server.
    protected void exitServer (PrintWriter out, Socket clientSocket) {
        out.println(ServerResponses.BYE.getResponse());

        System.out.println("Client " + clientSocket.getInetAddress() + " disconnected...");
    }

    protected void setErrorOnClient (PrintWriter out, String command) {
        out.println(ServerResponses.UNKNOWN.getResponse());
    }
}

