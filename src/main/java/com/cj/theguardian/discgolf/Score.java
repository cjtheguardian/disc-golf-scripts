package com.cj.theguardian.discgolf;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Score {

    private Player player;
    private int strokes;
    private Map<String, Integer> strokePerHole = new TreeMap<>();

    public int getStrokes() {
        return strokes;
    }

    public void setStrokes(int strokes) {
        this.strokes = strokes;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setScoreForHole(String hole, Integer score) {
        strokePerHole.put(hole, score);
    }

    public int getTotalHoles() {
        return strokePerHole.keySet().size();
    }

    public Set<String> getHoleNames(){
        return new HashSet<>(strokePerHole.keySet());
    }

    public int getScoreForHole(String hole) {
        return strokePerHole.get(hole);
    }
}
