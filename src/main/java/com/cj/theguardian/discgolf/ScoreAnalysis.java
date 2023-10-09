package com.cj.theguardian.discgolf;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ScoreAnalysis {
    private Player playerOne;
    private Player playerTwo;

    private int countPlayerOneWins;
    private int countPlayerTwoWins;
    private int countTied;

    private int countPlayerOneStrokes;
    private int countPlayerTwoStrokes;

    private int countPlayerOneMarginOfVictory;
    private int countPlayerTwoMarginOfVictory;


    public ScoreAnalysis(Player playerOneName, Player playerTwoName) {
        this.playerOne = playerOneName;
        this.playerTwo = playerTwoName;
    }

    public void analyzeRound(Round round) {
        Integer playerOneScore = round.getScore(playerOne);
        Integer playerTwoScore = round.getScore(playerTwo);

        if(playerOneScore != null && playerTwoScore != null) {
            bumpStats(playerOneScore, playerTwoScore);
        }
    }

    private void bumpStats(int playerOneScore, int playerTwoScore) {
        countPlayerOneStrokes += playerOneScore;
        countPlayerTwoStrokes += playerTwoScore;
        if(playerOneScore < playerTwoScore) {
            countPlayerOneWins++;
            countPlayerOneMarginOfVictory += (playerTwoScore - playerOneScore);
        } else if (playerTwoScore < playerOneScore) {
            countPlayerTwoWins++;
            countPlayerTwoMarginOfVictory += (playerOneScore - playerTwoScore);
        } else {
            countTied++;
        }
    }

    public void printRoundWonStats() {
        System.out.println(playerOne + " won: " + countPlayerOneWins);
        System.out.println(playerTwo + " won: " + countPlayerTwoWins);
        System.out.println("Tied " + countTied);
    }

    public void printStrokesStats() {
        int totalRounds = totalRounds();
        System.out.println(playerOne + " strokes: " + countPlayerOneStrokes);
        System.out.println(playerTwo + " strokes: " + countPlayerTwoStrokes);
        Player winningPlayer = null;
        if(countPlayerOneStrokes < countPlayerTwoStrokes) {
            winningPlayer = playerOne;
        } else {
            winningPlayer = playerTwo;
        }

        System.out.println(winningPlayer + " has " + Math.abs(countPlayerOneStrokes - countPlayerTwoStrokes) + " less strokes over " + totalRounds + " total rounds");
        System.out.println("thats an average of " + strokeAdvantagePerRound() + " strokes per round");

        System.out.println("when " + playerOne + " wins, the average win margin is " + winMargin(countPlayerOneMarginOfVictory, countPlayerOneWins));
        System.out.println("when " + playerTwo + " wins, the average win margin is " + winMargin(countPlayerTwoMarginOfVictory, countPlayerTwoWins));
    }

    private BigDecimal strokeAdvantagePerRound() {
        int totalStrokeDiff = Math.abs(countPlayerOneStrokes - countPlayerTwoStrokes);
        return new BigDecimal(totalStrokeDiff).divide(new BigDecimal(totalRounds()), 2, RoundingMode.HALF_UP);
    }

    private int totalRounds() {
        return countPlayerOneWins + countPlayerTwoWins + countTied;
    }

    private BigDecimal winMargin(int mov, int totWins) {
        return new BigDecimal(mov).divide(new BigDecimal(totWins), 2, RoundingMode.HALF_UP);
    }

    public void printHoleStats() {
        printHoleStats(1);
    }


    private void printHoleStats(int holeNumber) {
        printHoleStats("" + holeNumber);
    }
    private void printHoleStats(String holeNumber) {

    }
}
