package com.cj.theguardian.discgolf;

import java.util.List;

public class CasitasAnalysis {

    public static void main(String[] args) throws Exception {
        List<Round> rounds = UDiscCsvParser.parseCsv("src/main/resources/dg/udisc_scorecards_20230720.csv");

        rounds = RoundFilters.applyFilters(rounds, RoundFilters.casitas(), RoundFilters.hasPlayers(Player.PAT, Player.CONOR));

        LayoutAnalysis conorsAnalysis = new LayoutAnalysis(Player.CONOR);
        LayoutAnalysis patsAnalysis = new LayoutAnalysis(Player.PAT);
        System.out.println("Rounds: " + rounds.size());
        int conorBirdiesWhenPatBogies = 0;
        int patBirdiesWhenConorBogies = 0;
        int totalHoles = 0;
        for(Round round : rounds) {
            conorsAnalysis.applyRound(round);
            patsAnalysis.applyRound(round);
            for(Hole hole : round.getHoles()) {
                boolean conorBirdied = hole.isUnderPar(Player.CONOR);
                boolean patBirdied = hole.isUnderPar(Player.PAT);

                boolean conorBogeyed = hole.isOverPar(Player.CONOR);
                boolean patBogeyed = hole.isOverPar(Player.PAT);

                if(conorBirdied && patBogeyed) {
                    conorBirdiesWhenPatBogies++;
                }
                if(conorBogeyed && patBirdied) {
                    patBirdiesWhenConorBogies++;
                }
                totalHoles++;
            }
        }
        System.out.println("conor birdies when pat bogeys: " + conorBirdiesWhenPatBogies);
        System.out.println("pat birdies when conor bogeys: " + patBirdiesWhenConorBogies);
        System.out.println("total holes: " + totalHoles);
        for(int i = 1; i <= 18 ;i ++) {
            HoleAnalysis.HoleStats conorsStats = conorsAnalysis.getStatsForHole("" + i);
            HoleAnalysis.HoleStats patsStats = patsAnalysis.getStatsForHole("" + i);

            System.out.println("Hole " + i);
            System.out.println("=============");
            System.out.println("Conor: ");
            conorsStats.print("\t");
            System.out.println();
            System.out.println("Pat: ");
            patsStats.print("\t");
            System.out.println("=============");
            System.out.println();
        }


    }


}
