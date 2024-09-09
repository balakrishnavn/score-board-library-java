package com.sportradar;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementation of the ScoreBoard interface to manage football matches.
 * It provides functionality to start, update, finish matches, and retrieve a summary of ongoing matches.
 */
public class ScoreBoardImpl implements ScoreBoard {

    /**
     * A map that holds ongoing matches using a unique key combining home and away team names.
     */
    private final Map<String, Match> ongoingMatches = new HashMap<>();

    /**
     * Starts a new match with the given home and away teams.
     * Initializes the match with a score of 0-0 and adds it to the scoreboard.
     *
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @throws MatchAlreadyExistsException if a match between the specified teams already exists.
     */
    @Override
    public void startMatch(String homeTeam, String awayTeam) {
        checkParameters(homeTeam, awayTeam);
        String matchKey = formMatchKey(homeTeam, awayTeam);
        if (isMatchOngoing(matchKey)) {
            throw new MatchAlreadyExistsException(homeTeam, awayTeam);
        }

        ongoingMatches.put(matchKey, new Match(homeTeam, awayTeam));
    }

    /**
     * Updates the score of an ongoing match identified by the home and away teams.
     *
     * @param homeTeam Name of the home team.
     * @param homeScore New score of the home team.
     * @param awayTeam Name of the away team.
     * @param awayScore New score of the away team.
     * @throws MatchDoesNotExistException if no match exists between the specified teams.
     */
    @Override
    public void updateScore(String homeTeam, int homeScore, String awayTeam, int awayScore) {
        checkParameters(homeTeam, awayTeam);

        String matchKey = formMatchKey(homeTeam, awayTeam);
        if (!isMatchOngoing(matchKey)) {
            throw new MatchDoesNotExistException(homeTeam, awayTeam);
        }

        Match match = ongoingMatches.get(matchKey);
        match.setHomeScore(Math.abs(homeScore));
        match.setAwayScore(Math.abs(awayScore));
    }

    /**
     * Finishes an ongoing match and removes it from the scoreboard.
     *
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @throws MatchDoesNotExistException if no match exists between the specified teams.
     */
    @Override
    public void finishMatch(String homeTeam, String awayTeam) {
        checkParameters(homeTeam, awayTeam);

        String matchKey = formMatchKey(homeTeam, awayTeam);
        if (!isMatchOngoing(matchKey)) {
            throw new MatchDoesNotExistException(homeTeam, awayTeam);
        }

        ongoingMatches.remove(matchKey);
    }

    /**
     * Retrieves a summary of all ongoing matches, ordered by total score in descending order.
     * If matches have the same total score, they are ordered by their start time, with the most recent first.
     *
     * @return A list of ongoing matches sorted by total score and start time.
     */
    @Override
    public List<Match> getSummary() {
        List<Match> matchList = new ArrayList<>(ongoingMatches.values());
        matchList.sort((m1, m2) -> {
            // Compare by total score (descending)
            int scoreComparison = Integer.compare(m2.getTotalScore(), m1.getTotalScore());
            if (scoreComparison == 0) {
                // If scores are the same, compare by start time (most recent first)
                return m2.getStartTime().compareTo(m1.getStartTime());
            }
            return scoreComparison;
        });
        return matchList;
    }

    /**
     * Checks if a match with the given key is currently ongoing.
     *
     * @param matchKey The unique key representing the match.
     * @return true if the match is ongoing, false otherwise.
     */
    private boolean isMatchOngoing(String matchKey) {
        return ongoingMatches.containsKey(matchKey);
    }

    /**
     * Validates that the home and away team names are not null.
     *
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @throws InvalidParameterException if either team name is null.
     */
    private void checkParameters(String homeTeam, String awayTeam) {
        if (homeTeam == null || awayTeam == null) {
            throw new InvalidParameterException("One of the parameters is null");
        }
    }

    /**
     * Forms a unique key for the match by combining the home and away team names.
     *
     * @param homeTeam Name of the home team.
     * @param awayTeam Name of the away team.
     * @return A string key representing the match.
     */
    private String formMatchKey(String homeTeam, String awayTeam) {
        return homeTeam + "," + awayTeam;
    }

    /**
     * Retrieves the map of ongoing matches.
     *
     * @return A map containing ongoing matches with their keys.
     */
    Map<String, Match> getMatches() {
        return ongoingMatches;
    }
}