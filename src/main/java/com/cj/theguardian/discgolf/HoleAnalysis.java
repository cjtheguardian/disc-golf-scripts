package com.cj.theguardian.discgolf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class HoleAnalysis {

    private List<HoleScore> scores = new ArrayList<>();


    public void addScore(HoleScore score) {
        scores.add(score);
    }

    public HoleStats getStats() {
        HoleStats stats = new HoleStats();
        int total = 0;
        for(HoleScore score : scores) {
            total += score.getScore();
            int relative = score.getScore() - score.getPar();
            if(relative < 0) {
                stats.birdies++;
            } else if (relative == 0) {
                stats.pars++;
            } else if (relative == 1) {
                stats.bogies++;
            } else if (relative == 2) {
                stats.doubleBogies++;
            } else {
                stats.tripleOrWorse++;
            }
        }
        stats.average = BigDecimal.valueOf(total).divide(BigDecimal.valueOf(scores.size()), 2, RoundingMode.HALF_UP);
        return stats;
    }

    public static class HoleStats {

        private int birdies;
        private int pars;
        private int bogies;
        private int doubleBogies;
        private int tripleOrWorse;

        private BigDecimal average;

        public int getBirdies() {
            return birdies;
        }

        public int getPars() {
            return pars;
        }

        public int getBogies() {
            return bogies;
        }

        public int getDoubleBogies() {
            return doubleBogies;
        }

        public int getTripleOrWorse() {
            return tripleOrWorse;
        }

        public BigDecimal getAverage() {
            return average;
        }

        public void print(String prefixLine) {
            System.out.println(prefixLine + "Birdies:    "+birdies);
            System.out.println(prefixLine + "            "+calcPercentage(birdies)+"%");
            System.out.println(prefixLine + "Pars:       "+pars);
            System.out.println(prefixLine + "            "+calcPercentage(pars)+"%");
            System.out.println(prefixLine + "Bogies:     "+bogies);
            System.out.println(prefixLine + "            "+calcPercentage(bogies)+"%");
            System.out.println(prefixLine + "2x Bogies:  "+doubleBogies);
            System.out.println(prefixLine + "            "+calcPercentage(doubleBogies)+"%");
            System.out.println(prefixLine + "3x+ Bogies: "+tripleOrWorse);
            System.out.println(prefixLine + "            "+calcPercentage(tripleOrWorse)+"%");
            System.out.println(prefixLine + "Average:    "+average);
        }

        private BigDecimal calcPercentage(int numerator) {
            return BigDecimal.valueOf(100*numerator).divide(BigDecimal.valueOf(birdies+pars+bogies+doubleBogies+tripleOrWorse), 1, RoundingMode.HALF_UP);

        }
    }



}
