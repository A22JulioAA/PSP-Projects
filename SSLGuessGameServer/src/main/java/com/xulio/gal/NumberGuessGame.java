package com.xulio.gal;

public class NumberGuessGame {
    private int numberToGuess;
    private boolean guessed;

    public NumberGuessGame() {
        this.numberToGuess = (int) (Math.random() * 100) + 1;
        this.guessed = false;
    }

    public String guess (int number, int tries) {
        if (number == numberToGuess) {
            guessed = true;

            return ServerResponses.WIN.getCODE() + " " + ServerResponses.WIN.getDESCRIPTION();
        } else if (number > numberToGuess) {
            return ServerResponses.HIGH.getCODE() + " " + ServerResponses.HIGH.getDESCRIPTION() + "< " + tries + " >\n";
        } else {
            return ServerResponses.LOW.getCODE() + " " + ServerResponses.LOW.getDESCRIPTION() + "< " + tries + " >\n";
        }
    }

    public int getNumberToGuess() {
        return numberToGuess;
    }

    public boolean isGuessed () {
        return guessed;
    }
}
