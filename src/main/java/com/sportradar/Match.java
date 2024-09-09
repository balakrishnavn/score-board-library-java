package com.sportradar;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Represents a football match with details such as team names, scores, and start time.
 * Provides functionality to track the scores of the home and away teams and calculate the total score.
 */
@Data
public class Match {
    /**
     * Name of the home team participating in the match.
     */
    private final String homeTeam;

    /**
     * Name of the away team participating in the match.
     */
    private final String awayTeam;

    /**
     * The start time of the match, set to the time when the match object is created.
     */
    private LocalDateTime startTime;

    /**
     * Current score of the home team.
     */
    private int homeScore;

    /**
     * Current score of the away team.
     */
    private int awayScore;

    /**
     * Constructs a new Match object with the specified home and away teams.
     * Initializes the start time to the current date and time.
     *
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     */
    public Match(String homeTeam, String awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.startTime = LocalDateTime.now();
    }

    /**
     * Calculates and returns the total score of the match by adding the home and away scores.
     *
     * @return The sum of home and away team scores.
     */
    public int getTotalScore() {
        return homeScore + awayScore;
    }
}