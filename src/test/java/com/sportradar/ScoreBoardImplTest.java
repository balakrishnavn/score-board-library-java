package com.sportradar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.InvalidParameterException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for the ScoreBoardImpl class, which implements the ScoreBoard interface.
 * This class verifies the functionality of starting, updating, finishing matches, and retrieving match summaries.
 */
public class ScoreBoardImplTest {
    private ScoreBoardImpl scoreBoard = new ScoreBoardImpl();

    /**
     * Test to ensure that starting a match with valid home and away teams
     * adds the match to the scoreboard.
     */
    @Test
    void givenHomeAndAwayTeams_whenStartMatch_thenSetContainsOneMatch() {
        scoreBoard.startMatch("Team1", "Team2");

        assertThat(scoreBoard.getMatches().size()).isEqualTo(1);
    }

    /**
     * Parameterized test to verify that starting a match with null parameters
     * throws an InvalidParameterException.
     *
     * @param team1 The name of the home team.
     * @param team2 The name of the away team.
     */
    @ParameterizedTest
    @MethodSource("startMatchParameterProvider")
    void givenNullParameter_whenStartMatch_thenThrowException(String team1, String team2) {
        assertThatThrownBy(() -> scoreBoard.startMatch(team1, team2)).isInstanceOf(InvalidParameterException.class);
    }

    /**
     * Test to verify that trying to start a match with a duplicate team pair
     * throws a MatchAlreadyExistsException.
     */
    @Test
    void givenDuplicateTeamPair_whenStartMatch_thenThrowException() {
        scoreBoard.startMatch("Team1", "Team2");

        assertThatThrownBy(() -> scoreBoard.startMatch("Team1", "Team2"))
                .isInstanceOf(MatchAlreadyExistsException.class);
    }

    /**
     * Parameterized test to ensure that updating a match's score with valid parameters
     * properly updates the scores in the scoreboard.
     *
     * @param score The score to be updated (can be positive or negative).
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, -3 })
    void givenMatchStartedAndValidParameters_whenUpdateScore_thenScoreShouldBeUpdated(int score) {
        scoreBoard.startMatch("Team1", "Team2");

        scoreBoard.updateScore("Team1", score, "Team2", score);

        Map<String, Match> matches = scoreBoard.getMatches();
        assertThat(matches.size()).isEqualTo(1);

        Match match = matches.get("Team1,Team2");
        assertThat(match.getHomeScore()).isEqualTo(Math.abs(score));
        assertThat(match.getAwayScore()).isEqualTo(Math.abs(score));
    }

    /**
     * Test to verify that updating the score of a non-existing match throws
     * a MatchDoesNotExistException.
     */
    @Test
    void givenMatchDoesNotExistAndValidParameters_whenUpdateScore_thenThrowException() {
        assertThatThrownBy(() -> scoreBoard.updateScore("Team1", 10, "Team2", 10))
                .isInstanceOf(MatchDoesNotExistException.class);
    }

    /**
     * Parameterized test to ensure that updating a match's score with null parameters
     * throws an InvalidParameterException.
     *
     * @param team1 The name of the home team.
     * @param team1Score The score of the home team.
     * @param team2 The name of the away team.
     * @param team2Score The score of the away team.
     */
    @ParameterizedTest
    @MethodSource("updateScoreParameterProvider")
    void givenNullParameter_whenUpdateScore_thenThrowException(String team1, int team1Score, String team2,
                                                               int team2Score) {
        scoreBoard.startMatch("Team1", "Team2");

        assertThatThrownBy(() -> scoreBoard.updateScore(team1, team1Score, team2, team2Score))
                .isInstanceOf(InvalidParameterException.class);
    }

    /**
     * Test to verify that finishing a match correctly removes it from the scoreboard.
     */
    @Test
    void givenValidParameters_whenFinishMatch_thenThereIsNoOngoingMatches() {
        scoreBoard.startMatch("Team1", "Team2");

        scoreBoard.finishMatch("Team1", "Team2");

        assertThat(scoreBoard.getMatches().size()).isEqualTo(0);
    }

    /**
     * Test to verify that finishing a non-existing match throws a
     * MatchDoesNotExistException.
     */
    @Test
    void givenMatchDoesNotExist_whenFinishMatch_thenThrowException() {
        assertThatThrownBy(() -> scoreBoard.finishMatch("Team1", "Team2"))
                .isInstanceOf(MatchDoesNotExistException.class);
    }

    /**
     * Parameterized test to ensure that finishing a match with null parameters
     * throws an InvalidParameterException.
     *
     * @param team1 The name of the home team.
     * @param team2 The name of the away team.
     */
    @ParameterizedTest
    @MethodSource("finishMatchParameterProvider")
    void givenNullParameter_whenFinishMatch_thenThrowException(String team1, String team2) {
        scoreBoard.startMatch("Team1", "Team2");
        assertThatThrownBy(() -> scoreBoard.finishMatch(team1, team2)).isInstanceOf(InvalidParameterException.class);
    }

    /**
     * Test to verify that if no matches exist, the summary method returns an empty list.
     */
    @Test
    void givenNoMatchesExist_whenGetSummary_thenReturnEmptyList() {
        assertThat(scoreBoard.getSummary()).isEmpty();
    }

    /**
     * Test to verify that the summary of ongoing matches returns a list sorted by total score
     * in descending order and by start time (most recent first) if scores are tied.
     *
     * @throws InterruptedException if the thread is interrupted during the timeout test.
     */
    @Test
    void whenGetSummary_thenReturnListInProperOrder() throws InterruptedException {
        scoreBoard.startMatch("Mexico", "Canada");
        scoreBoard.startMatch("Spain", "Brazil");
        scoreBoard.startMatch("Germany", "France");

        // Introduce a slight delay to ensure different start times
        Assertions.assertTimeout(Duration.ofMillis(500), () -> {
            scoreBoard.startMatch("Uruguay", "Italy");
        });
        scoreBoard.startMatch("Argentina", "Australia");

        scoreBoard.updateScore("Mexico", 0, "Canada", 5);
        scoreBoard.updateScore("Spain", 10, "Brazil", 2);
        scoreBoard.updateScore("Germany", 2, "France", 2);
        scoreBoard.updateScore("Uruguay", 6, "Italy", 6);
        scoreBoard.updateScore("Argentina", 3, "Australia", 1);

        List<Match> summary = scoreBoard.getSummary();
        assertEquals("Uruguay", summary.get(0).getHomeTeam());
        assertEquals("Italy", summary.get(0).getAwayTeam());

        assertEquals("Spain", summary.get(1).getHomeTeam());
        assertEquals("Brazil", summary.get(1).getAwayTeam());

        assertEquals("Mexico", summary.get(2).getHomeTeam());
        assertEquals("Canada", summary.get(2).getAwayTeam());

        assertEquals("Argentina", summary.get(3).getHomeTeam());
        assertEquals("Australia", summary.get(3).getAwayTeam());

        assertEquals("Germany", summary.get(4).getHomeTeam());
        assertEquals("France", summary.get(4).getAwayTeam());
    }

    /**
     * Provides test parameters for the startMatch method where one or both team names are null.
     *
     * @return A stream of arguments for testing null parameters in startMatch.
     */
    private static Stream<Arguments> startMatchParameterProvider() {
        return Stream.of(
                Arguments.of("Team1", null),
                Arguments.of(null, "Team2"),
                Arguments.of(null, null));
    }

    /**
     * Provides test parameters for the updateScore method where one or both parameters are null.
     *
     * @return A stream of arguments for testing null parameters in updateScore.
     */
    private static Stream<Arguments> updateScoreParameterProvider() {
        return Stream.of(
                Arguments.of("Team1", 0, null, 1),
                Arguments.of(null, 1, "Team2", 0),
                Arguments.of(null, 2, null, 3));
    }

    /**
     * Provides test parameters for the finishMatch method where one or both team names are null.
     *
     * @return A stream of arguments for testing null parameters in finishMatch.
     */
    private static Stream<Arguments> finishMatchParameterProvider() {
        return Stream.of(
                Arguments.of("Team1", null),
                Arguments.of(null, "Team2"),
                Arguments.of(null, null));
    }
}