package com.cj.theguardian.discgolf;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Round {

    private Layout layout;
    private LocalDateTime date;
    private List<Score> players;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    public List<Score> getPlayers() {
        return players;
    }

    public void setPlayers(List<Score> players) {
        this.players = players;
    }

    public Integer getScore(Player player) {
        return players.stream().filter(p -> p.getPlayer().equals(player)).findFirst().map(Score::getStrokes).orElse(null);

    }

    public int totalHoles() {
        return players.get(0).getTotalHoles();
    }

    public List<Hole> getHoles() {
        List<Hole> holes = layout.setupHoles();
        for(Hole hole : holes) {
            for(Score player : players) {
                hole.setScore(player.getPlayer(), player.getScoreForHole(hole.getName()));
            }
        }
        return holes;
    }

    public boolean isClean(Player player) {
        Score score = players.stream().filter(p -> p.getPlayer() == player).findFirst().orElse(null);
        if(score == null) {
            return false;
        }
        for(String hole : score.getHoleNames()) {
            int playerScore = score.getScoreForHole(hole);
            int par = layout.getParForHole(hole);
            if(playerScore-par > 0) {
                return false;
            }
        }
        return true;
    }
}
