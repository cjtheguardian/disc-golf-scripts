package com.cj.theguardian.discgolf.ratings;

import com.cj.theguardian.discgolf.Round;
import com.cj.theguardian.utils.CsvReader;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RatingsCsvParser {

    public static List<RoundRating> parseCsv(String classpath) throws Exception {
        CsvReader csvReader = new CsvReader(new File(classpath), true);
        Map<String,String> nextRow = null;
        List<RoundRating> ratings = new ArrayList<>();
        // date,rating,tourney,round
        while((nextRow = csvReader.nextRowAsMap()) != null) {
            RoundRating rating = new RoundRating();
            rating.setDate(LocalDate.parse(nextRow.get("date")));
            rating.setRating(Integer.parseInt(nextRow.get("rating")));
            rating.setTournamentName(nextRow.get("tourney"));
            rating.setRoundNumber(Integer.parseInt(nextRow.get("round")));
            ratings.add(rating);
        }
        return ratings;
    }

}
