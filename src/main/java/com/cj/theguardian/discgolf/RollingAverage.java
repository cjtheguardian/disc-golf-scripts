package com.cj.theguardian.discgolf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class RollingAverage {


    public static void main(String[] args) throws Exception {
        List<Round> rounds = UDiscCsvParser.parseCsv("src/main/resources/dg/udisc_scorecards_20230709.csv");
        Player player = Player.PAT;
        rounds = RoundFilters.applyFilters(rounds, RoundFilters.casitas(), RoundFilters.hasPlayers(player));
        List<BigDecimal> rollingAverages = new ArrayList<>();
        for(int i =0;i<rounds.size();i++) {
            int total = 0;
            int roundsIncluded = 0;
            for(int j = i-4;j <= i;j++) {
                if(j >= 0) {
                    int score = rounds.get(j).getScore(player);
                    total += score;
                    roundsIncluded++;
                }
            }
            BigDecimal rollingAverage = BigDecimal.valueOf(total).divide(BigDecimal.valueOf(roundsIncluded), 2, RoundingMode.HALF_UP);
            System.out.println("Rolling average for " + rounds.get(i).getDate() + " is " + rollingAverage);
            rollingAverages.add(rollingAverage);
        }
        int indexOfLessThan10 = -1;
        for(int i =0 ; i < rollingAverages.size();i++) {
            if(rollingAverages.get(i).compareTo(BigDecimal.valueOf(64)) < 0) {
                indexOfLessThan10 = i;
                break;
            }
        }
        System.out.println(rounds.get(indexOfLessThan10).getDate());
    }

}
