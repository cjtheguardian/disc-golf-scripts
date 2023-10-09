package com.cj.theguardian.discgolf;

import java.time.LocalDateTime;
import java.util.List;

public class WolfHillAnalysis {

    public static void main(String[] args) throws Exception {
        List<Round> rounds = UDiscCsvParser.parseCsv("src/main/resources/dg/udisc_scorecards_20230720.csv");

        rounds = RoundFilters.applyFilters(rounds, RoundFilters.wolfHill(), RoundFilters.hasPlayers(Player.CONOR), RoundFilters.since(LocalDateTime.parse("2022-12-15T00:00:00")));

        LayoutAnalysis conorsAnalysis = new LayoutAnalysis(Player.CONOR);

        for(Round round : rounds) {
            conorsAnalysis.applyRound(round);
        }
        for(int i = 1; i <= 18 ;i ++) {
            HoleAnalysis.HoleStats conorsStats = conorsAnalysis.getStatsForHole("" + i);

            System.out.println("Hole " + i);
            System.out.println("=============");
            System.out.println("Conor: ");
            conorsStats.print("\t");
            System.out.println();

        }

    }

}
