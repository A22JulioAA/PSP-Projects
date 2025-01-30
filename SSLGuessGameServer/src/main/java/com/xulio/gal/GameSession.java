package com.xulio.gal;

public class GameSession {
    private final NumberGuessGame game;
    private int tries;

    public GameSession (int tries) {
        this.tries = tries;
        this.game = new NumberGuessGame();
    }

    public String processGuess (int guess) {
        if (tries <= 0) return ServerResponses.LOSE_NUM.getResponse();
        tries--;
        return game.guess(guess, tries);
    }

    public boolean isGameOver () {
        return tries <= 0 || game.isGuessed();
    }
}
