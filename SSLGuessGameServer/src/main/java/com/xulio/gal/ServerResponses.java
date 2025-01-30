package com.xulio.gal;

public enum ServerResponses {
    SERVER_READY (10, "Game server ready"),
    BYE (11, "BYE"),
    SERVER_READY_BEFORE_ERROR (15, "Game server ready before communication error"),
    PLAY (20, "The game starts with X tries"),
    LOW (25, "Your guess is lower than the number"),
    HIGH (35, "Your guess is higher than the number"),
    INFO (40, "Informs the client about the game"),
    WIN (50, "The client win the game"),
    LOSE_NUM (70, "The client has no more attempts to guess the number"),
    ERR (80, "The command cannot be user at this time"),
    UNKNOWN (90, "The command used is incorrect");

    private final int CODE;
    private final String DESCRIPTION;

    ServerResponses(int CODE, String DESCRIPTION) {
        this.CODE = CODE;
        this.DESCRIPTION = DESCRIPTION;
    }

    public int getCODE() {
        return CODE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public String getResponse () { return CODE + " " + DESCRIPTION; }
}
