package com.sportradar;

/**
 * Custom exception thrown when an attempt is made to create a match that already exists.
 */
public class MatchAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new `MatchAlreadyExistsException` with a detailed error message.
     *
     * @param homeTeam Name of the home team involved in the existing match.
     * @param awayTeam Name of the away team involved in the existing match.
     */
    public MatchAlreadyExistsException(String homeTeam, String awayTeam) {
        // Calls the constructor of RuntimeException with a formatted error message
        super(String.format("Match between '%s' and '%s' already exists", homeTeam, awayTeam));
    }

}
