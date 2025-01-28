package com.xulio.gal;

public class NumberGuessGame {
    private int numberToGuess;
    private boolean guessed;

    public NumberGuessGame() {
        this.numberToGuess = (int) (Math.random() * 100) + 1;
        this.guessed = false;
    }

    public String guess (int number) {
        if (number == numberToGuess) {
            guessed = true;

            return number + " is correct!";
        } else if (number > numberToGuess) {
            return "The number is lower than " + number;
        } else {
            return "The number is higher than " + number;
        }
    }

    public boolean isGuessed () {
        return guessed;
    }
}
