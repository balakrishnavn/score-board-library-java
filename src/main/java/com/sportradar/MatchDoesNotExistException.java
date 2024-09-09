package com.sportradar;

/**
 * Custom exception thrown when an attempt is made to perform an operation on a match
 * that does not exist in the scoreboard.
 */
public class MatchDoesNotExistException extends RuntimeException {

    /**
     * Constructs a new `MatchDoesNotExistException` with a detailed error message
     * indicating the names of the home and away teams for the match that does not exist.
     *
     * @param homeTeam Name of the home team involved in the missing match.
     * @param awayTeam Name of the away team involved in the missing match.
     */
    public MatchDoesNotExistException(String homeTeam, String awayTeam) {
        // Calls the constructor of RuntimeException with a formatted error message
        super(String.format("Match between '%s' and '%s' does not exist", homeTeam, awayTeam));
    }
}