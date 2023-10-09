package com.cj.theguardian.discgolf.ratings;

import com.cj.theguardian.discgolf.utils.MathUtils;
import com.cj.theguardian.utils.ApacheMathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RatingsCalculator {

    private static Comparator<RoundRating> sortedInDescDate = comparator();

    private static Comparator<RoundRating> comparator() {
        Comparator<RoundRating> byDate = Comparator.comparing(roundRating -> roundRating.getDate());
        byDate = byDate.reversed();
        Comparator<RoundRating> byRoundNumber = Comparator.comparing(roundRating -> roundRating.getRoundNumber());
        byRoundNumber = byRoundNumber.reversed();
        return byDate.thenComparing(byRoundNumber);
    }

    public static Integer calcRatingsAsOfDate(LocalDate asOfDate, List<RoundRating> roundRatings) {

        roundRatings = filterForQualifiedRounds(asOfDate, roundRatings);

        System.out.println("Using ratings: " + roundRatings);
        List<RoundRating> worthDouble = new ArrayList<>();
        if (roundRatings.size() >= 9) { // TODO this should be 9
            Integer numberOfRoundsWorthDouble = BigDecimal.valueOf(roundRatings.size()).setScale(2).multiply(BigDecimal.valueOf(0.25)).setScale(0, RoundingMode.CEILING).intValue();
            worthDouble = roundRatings.subList(0, numberOfRoundsWorthDouble);
            System.out.println("worth double: " + worthDouble);
        }

        List<BigDecimal> ratingsToAverage = Stream.concat(
                roundRatings.stream().map(rr -> rr.getRating()),
                worthDouble.stream().map(rr -> rr.getRating())
        ).map(i -> BigDecimal.valueOf(i))
                .collect(Collectors.toList());

        return MathUtils.calcAverage(ratingsToAverage).setScale(0, RoundingMode.HALF_UP).intValue();

    }

    private static List<RoundRating> filterForQualifiedRounds(LocalDate asOfDate, List<RoundRating> roundRatings) {
        roundRatings = roundRatings.stream().filter(rr -> !asOfDate.isBefore(rr.getDate())).collect(Collectors.toList());

        LocalDate oneYearAgo = asOfDate.minusYears(1);
        List<RoundRating> withinLastYear = roundRatings.stream().filter(rr -> rr.getDate().isAfter(oneYearAgo)).sorted(sortedInDescDate).collect(Collectors.toList());
        if (withinLastYear.size() < 8) {

            LocalDate twoYearsAgo = asOfDate.minusYears(2);
            List<RoundRating> withinLast2Year = roundRatings.stream().filter(rr -> rr.getDate().isAfter(twoYearsAgo)).sorted(sortedInDescDate).collect(Collectors.toList());
            if (withinLast2Year.size() > 8) {
                roundRatings = withinLast2Year.subList(0, 8);
            } else {
                roundRatings = withinLast2Year;
            }

        } else {
            roundRatings = withinLastYear;
        }

        if (roundRatings.size() >= 7) {
            List<BigDecimal> ratings = roundRatings.stream().map(rr -> rr.getRating()).map(rating -> BigDecimal.valueOf(rating).setScale(2, RoundingMode.HALF_UP)).collect(Collectors.toList());
            BigDecimal avg = MathUtils.calcAverage(ratings);
            BigDecimal stdDev = ApacheMathHelper.stdDeviation(ratings, false);
            BigDecimal stdDev2_5x = stdDev.multiply(BigDecimal.valueOf(2.5));

            System.out.println("avg: " + avg);
            System.out.println("std: " + stdDev);
            System.out.println("std x 2.5: " + stdDev2_5x);
            if(stdDev2_5x.compareTo(BigDecimal.valueOf(100)) > 0) {
                stdDev2_5x = BigDecimal.valueOf(100);
            }
            Integer minRoundRating = avg.subtract(stdDev2_5x).setScale(0, RoundingMode.CEILING).intValue();

            System.out.println("minRoundRating: " + minRoundRating);
            roundRatings = roundRatings.stream().filter(rr -> rr.getRating() >= minRoundRating).collect(Collectors.toList());
            //TODO, do i add in the next lowest round and recalculate?
        }
        return roundRatings;

    }

}
