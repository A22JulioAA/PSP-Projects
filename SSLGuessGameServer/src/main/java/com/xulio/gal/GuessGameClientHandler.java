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
        this.numberGuessGame = new NumberGuessGame();
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {

            out.println("Welcome!");
            out.println("Introduce your commands to play! If you don't know how game works, enter 'HELP'");

            while (!numberGuessGame.isGuessed()) {
                String line;
                line = in.readLine();

                try {
                    int guess = Integer.parseInt(line);
                    String response = numberGuessGame.guess(guess);
                    out.println(response);
                } catch (NumberFormatException e) {
                    out.println("Number invalid! Introduce other number...");
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
}

