package com.cj.theguardian.discgolf;

import java.util.List;

import static com.cj.theguardian.discgolf.RoundFilters.*;

public class WhoWonMore {

    public static void main(String[] args) throws Exception {
        List<Round> rounds = UDiscCsvParser.parseCsv("src/main/resources/dg/udisc_scorecards_20230709.csv");

//        showStatsMeVsPat("All Rounds (All Time)", applyFilters(rounds));
        showStatsMeVsPat("All Rounds - min 18 holes (All Time)", applyFilters(rounds, holeCount(18)));
        showStatsMeVsPat("Casitas (All Time)", applyFilters(rounds, casitas()));
        showStatsMeVsPat("Not Casitas (All Time)", applyFilters(rounds, casitas().negate()));
        showStatsMeVsPat("Not Casitas - min 18 holes (All Time)", applyFilters(rounds, casitas().negate(), holeCount(18)));

        printStatsByYear(rounds, 2021);
        printStatsByYear(rounds, 2022);
        printStatsByYear(rounds, 2023);

        showStatsMeVsPat("Since Pat Got Good - Casitas", applyFilters(rounds, casitas(), sincePatGotGood()));
        showStatsMeVsPat("Since Conor Got Good - Casitas", applyFilters(rounds, casitas(), sinceConorGotGood()));
//
//        showStatsMeVsPat("All Rounds (2023)", applyFilters(rounds, since(LocalDateTime.of(2023, 1, 1, 0, 0))));
//        showStatsMeVsPat("All Rounds - min 18 holes (2023)", applyFilters(rounds, since(LocalDateTime.of(2023, 1, 1, 0, 0)), holeCount(18)));
//        showStatsMeVsPat("Casitas (2023)", applyFilters(rounds, casitas(), since(LocalDateTime.of(2023, 1, 1, 0, 0))));
//        showStatsMeVsPat("Not Casitas (2023)", applyFilters(rounds, casitas().negate(), since(LocalDateTime.of(2023, 1, 1, 0, 0))));
//        showStatsMeVsPat("Not Casitas - min 18 holes (2023)", applyFilters(rounds, casitas().negate(), since(LocalDateTime.of(2023, 1, 1, 0, 0)), holeCount(18)));



    }

    private static void printStatsByYear(List<Round> rounds, int year) {

        showStatsMeVsPat("All Rounds - min 18 holes "+year, applyFilters(rounds, holeCount(18), byYear(year)));
        showStatsMeVsPat("Casitas " + year, applyFilters(rounds, casitas(), byYear(year)));
        showStatsMeVsPat("Not Casitas " + year, applyFilters(rounds, casitas().negate(), byYear(year)));
        showStatsMeVsPat("Not Casitas - min 18 holes " + year, applyFilters(rounds, casitas().negate(), holeCount(18), byYear(year)));
    }


    private static void showStatsMeVsPat(String filterDesc, List<Round> rounds) {
        ScoreAnalysis scoreAnalysis = new ScoreAnalysis(Player.CONOR, Player.PAT);

        for(Round round : rounds) {
            scoreAnalysis.analyzeRound(round);
        }

        System.out.println("***************************");
        System.out.println(filterDesc);
        scoreAnalysis.printRoundWonStats();
        scoreAnalysis.printStrokesStats();
        System.out.println("***************************\n\n");

    }

}
