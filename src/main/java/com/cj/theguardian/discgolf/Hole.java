package com.cj.theguardian.discgolf;

import java.util.HashMap;
import java.util.Map;

public class Hole {

    private String name;
    private Integer par;
    private Map<Player, Integer> scores = new HashMap<>();

    public Hole(String name, Integer par) {
        this.par = par;
        this.name = name;
    }

    public Integer getScore(Player player) {
        return scores.get(player);
    }

    public Integer getPar() {
        return par;
    }


    public String getName() {
        return name;
    }

    public void setScore(Player player, int scoreForHole) {
        scores.put(player, scoreForHole);
    }

    public boolean isUnderPar(Player player) {
        return scores.get(player) - par < 0;
    }


    public boolean isOverPar(Player player) {
        return scores.get(player) - par > 0;
    }
}
