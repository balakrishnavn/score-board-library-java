package com.sportradar;

import java.util.List;

/**
 * Interface representing the operations of a scoreboard for managing football matches.
 */
public interface ScoreBoard {

    /**
     * Starts a new match and adds it to the scoreboard.
     *
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     */
    void startMatch(String homeTeam, String awayTeam);

    /**
     * Updates the score of an ongoing match.
     *
     * @param homeTeam  Name of the home team.
     * @param homeScore New score of the home team.
     * @param awayTeam  Name of the away team.
     * @param awayScore New score of the away team.
     */
    void updateScore(String homeTeam, int homeScore, String awayTeam, int awayScore);

    /**
     * Finishes the specified match and removes it from the scoreboard.
     *
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     */
    void finishMatch(String homeTeam, String awayTeam);

    /**
     * Provides a summary of all ongoing matches, ordered by their total score.
     * Matches with the same total score are ordered by their start time, with the most recent first.
     *
     * @return List of ongoing matches, sorted as described.
     */
    List<Match> getSummary();
}
