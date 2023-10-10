package com.cj.theguardian.discgolf.ratings;

import com.cj.theguardian.discgolf.utils.MathUtils;
import com.cj.theguardian.utils.ApacheMathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToDoubleFunction;
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
        // only take the rounds before the as of date
        roundRatings = roundRatings.stream().filter(rr -> !asOfDate.isBefore(rr.getDate())).collect(Collectors.toList());

        roundRatings = filterForQualifiedRounds(roundRatings);

        System.out.println("Using ratings: " + roundRatings);
        List<RoundRating> worthDouble = new ArrayList<>();
        if (roundRatings.size() >= 9) {
            Integer numberOfRoundsWorthDouble = BigDecimal.valueOf(roundRatings.size()).setScale(2).multiply(BigDecimal.valueOf(0.25)).setScale(0, RoundingMode.HALF_UP).intValue();
            worthDouble = roundRatings.subList(0, numberOfRoundsWorthDouble);
            System.out.println("worth double: " + worthDouble);
        }

        List<WeightedRating> weightedRatings = Stream.concat(
                roundRatings.stream().map(rr -> weightedRating(rr)),
                worthDouble.stream().map(rr -> weightedRating(rr))
        )
                .collect(Collectors.toList());

        ToDoubleFunction<BigDecimal> toDouble = bd -> bd.doubleValue();
        double sumWeighted = weightedRatings.stream().map(wr -> wr.weightedRating()).collect(Collectors.summingDouble(toDouble));
        double totalWeight = weightedRatings.stream().map(wr -> wr.weight).collect(Collectors.summingDouble(toDouble));

        BigDecimal finalRating = BigDecimal.valueOf(sumWeighted).divide(BigDecimal.valueOf(totalWeight), 0, RoundingMode.HALF_UP);
        System.out.println("rating: " + finalRating);
        return finalRating.intValue();

    }

    private static WeightedRating weightedRating(RoundRating rr) {
        int holes = rr.getHoles();
        BigDecimal weight = BigDecimal.valueOf(holes).divide(BigDecimal.valueOf(18), 3, RoundingMode.HALF_UP);
        WeightedRating rating = new WeightedRating();
        rating.rawRating = BigDecimal.valueOf(rr.getRating());
        rating.weight = weight;
        return rating;
    }

    private static class WeightedRating {
        BigDecimal rawRating;
        BigDecimal weight;

        public BigDecimal weightedRating() {
            return rawRating.multiply(weight);
        }
    }

    private static List<RoundRating> filterForQualifiedRounds(List<RoundRating> roundRatings) {

        LocalDate latestRoundDate = roundRatings.stream().map(RoundRating::getDate).max(Comparator.naturalOrder()).orElse(null);
        if(latestRoundDate == null) {
            return new ArrayList<>();
        }
        LocalDate oneYearAgo = latestRoundDate.minusYears(1);
        List<RoundRating> withinLastYear = roundRatings.stream().filter(rr -> rr.getDate().isAfter(oneYearAgo)).sorted(sortedInDescDate).collect(Collectors.toList());
        if (withinLastYear.size() < 8) {

            LocalDate twoYearsAgo = latestRoundDate.minusYears(2);
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
