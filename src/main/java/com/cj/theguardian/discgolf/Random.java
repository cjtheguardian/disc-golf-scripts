package com.cj.theguardian.discgolf;

import java.time.Month;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Random {

    public static void main(String[] args) throws Exception {
        List<Round> rounds = UDiscCsvParser.parseCsv("src/main/resources/dg/udisc_scorecards_20230720.csv");

        rounds = RoundFilters.applyFilters(rounds);


        rounds = rounds.stream().sorted(Comparator.comparing(Round::getDate)).collect(Collectors.toList());

        List<Round> patCleanRounds = rounds.stream().filter(r -> r.isClean(Player.PAT)).collect(Collectors.toList());
        List<Round> conorCleanRounds = rounds.stream().filter(r -> r.isClean(Player.CONOR)).collect(Collectors.toList());

        System.out.println("Pat: " + patCleanRounds.size());
        System.out.println("Conor: " + conorCleanRounds.size());

        System.out.println("Pat's clean rounds");
        printRounds(patCleanRounds);

        System.out.println("Conor's clean rounds");
        printRounds(conorCleanRounds);

    }

    private static void printRounds(List<Round> rounds) {
        rounds.forEach(r -> printRound(r));
    }

    private static void printRound(Round r) {
        System.out.println(r.getDate() + " : " + r.getLayout().getCourse().getName() + " : " + r.getLayout().getName());
    }


    private static String cat(Round round) {
        int year = round.getDate().getYear();
        String quarter = getQuarter(round.getDate().getMonth());
        return "" + year+"_"+quarter;
    }

    private static String getQuarter(Month month) {
        switch (month) {
            case JANUARY:
            case FEBRUARY:
            case MARCH:
                return "Q1";
            case APRIL:
            case MAY:
            case JUNE:
                return "Q2";
            case JULY:
            case AUGUST:
            case SEPTEMBER:
                return "Q3";
            case OCTOBER:
            case NOVEMBER:
            case DECEMBER:
                return "Q4";
            default : throw new RuntimeException("illegal month");
        }

    }
}
