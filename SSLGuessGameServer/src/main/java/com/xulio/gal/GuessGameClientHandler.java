package com.xulio.gal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GuessGameClientHandler implements Runnable {
    private Socket clientSocket;
    private NumberGuessGame numberGuessGame;

    public GuessGameClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {

            out.println(ServerResponses.SERVER_READY.getCODE() + " " + ServerResponses.SERVER_READY.getDESCRIPTION());
            out.println("Welcome!");
            out.println("Introduce your commands to play! If you don't know how game works, enter 'HELP'");

            String command;

            while ((command = in.readLine()) != null) {
                switch (command.toUpperCase()) {
                    case "NEW":
                        startNewGame(in, out, 6);
                        break;
                    case "NUM":
                        break;
                    case "HELP":
                        showHelpMessage(out);
                        break;
                    case "QUIT":
                        exitServer(out);
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
            throw new RuntimeException(e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Function to display Initial Menu.
    protected void showMainMenu ( )

    // Function to show HELP message.
    protected void showHelpMessage (PrintWriter out) {
        String message = "--------------- HELP ---------------\n" +
                "NEW -> New game. It accepts a parameter, the number of tries you want to guess the number.\n" +
                "Example: NEW 8\n" +
                "NUM -> The guess number. A game must be created before this command.\n" +
                "Example: NUM 44\n" +
                "HELP -> Menu to display help information.\n" +
                "QUIT -> Exit the program.\n";

        out.println(message);
    }

    protected void startNewGame (BufferedReader in, PrintWriter out, int tries) throws IOException {
        this.numberGuessGame = new NumberGuessGame();

        System.out.println("Guess number to player " + clientSocket.getInetAddress() +
                " is: " + numberGuessGame.getNumberToGuess());

        out.println(ServerResponses.PLAY.getCODE() + " " + ServerResponses.PLAY.getDESCRIPTION() + "< " + tries + " >");

        while (!numberGuessGame.isGuessed()) {
            if (tries > 0) {
                out.println("Guess the number...");
                out.flush();

                String line;
                line = in.readLine();

                if (line != null && line.toUpperCase().startsWith("NUM ")) {
                    String[] parts = line.split(" ");

                    if (parts.length == 2) {
                        try {
                            int guess = Integer.parseInt(parts[1]);
                            String response = numberGuessGame.guess(guess, tries);
                            out.println(response);
                        } catch (NumberFormatException e) {
                            out.println("Invalid");
                            out.println(ServerResponses.SERVER_READY_BEFORE_ERROR.getCODE() + " " +
                                    ServerResponses.SERVER_READY_BEFORE_ERROR.getDESCRIPTION());
                        }
                    }
                } else {
                    out.println("Invalid Format");
                    out.println(ServerResponses.SERVER_READY_BEFORE_ERROR.getCODE() + " " +
                            ServerResponses.SERVER_READY_BEFORE_ERROR.getDESCRIPTION());
                }

                tries--;
            } else {
                out.println(ServerResponses.LOSE_NUM.getCODE() + " " + ServerResponses.LOSE_NUM.getDESCRIPTION() +
                        "< " + numberGuessGame.getNumberToGuess() + ">");
                break;
            }

        }
    }

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
            out.println(ServerResponses.UNKNOWN.getCODE() + " " + ServerResponses.UNKNOWN.getDESCRIPTION());
        }
    }

    protected void exitServer (PrintWriter out) {
        out.println(ServerResponses.BYE.getCODE() + " " + ServerResponses.BYE.getDESCRIPTION());
    }

    protected void setErrorOnClient (PrintWriter out, String command) {
        out.println(ServerResponses.UNKNOWN.getCODE() + " " + ServerResponses.UNKNOWN.getDESCRIPTION());
        out.println("'" + command + "' is incorrect...");
    }
}

