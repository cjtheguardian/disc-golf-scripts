package com.cj.theguardian.discgolf;

public class HoleScore {

    private String hole;
    private int par;
    private int score;

    public HoleScore(String holeName, int par, int score) {
        this.par = par;
        this.score = score;
        this.hole = holeName;
    }

    public String getHole() {
        return hole;
    }

    public int getPar() {
        return par;
    }

    public int getScore() {
        return score;
    }
}
