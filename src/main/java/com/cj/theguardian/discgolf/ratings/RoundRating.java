package com.cj.theguardian.discgolf.ratings;

import java.time.LocalDate;

public class RoundRating {

    private LocalDate date;
    private Integer rating;
    private String tournamentName;
    private Integer roundNumber;
    private Integer holes;

    public Integer getHoles() {
        return holes;
    }

    public void setHoles(Integer holes) {
        this.holes = holes;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public Integer getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(Integer roundNumber) {
        this.roundNumber = roundNumber;
    }

    @Override
    public String toString() {
        return "\n\t" + date +
                " " + rating +
                " " + tournamentName +
                " " + roundNumber ;
    }
}
