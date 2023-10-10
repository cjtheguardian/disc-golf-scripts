package com.cj.theguardian.discgolf.ratings;

import com.cj.theguardian.discgolf.Round;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CalcRating {


    public static void main(String[] args) throws Exception{
        List<RoundRating> ratings = RatingsCsvParser.parseCsv("src/main/resources/dg/pdga_ratings.csv");
        ratings = ratings.stream().filter(rating -> rating.getDate().isAfter(LocalDate.parse("2013-04-01"))).collect(Collectors.toList());
        System.out.println("all ratings: " + ratings);
        Integer rating = RatingsCalculator.calcRatingsAsOfDate(LocalDate.parse("2023-10-11"), ratings);
        System.out.println("final rating: " + rating);

    }

}
