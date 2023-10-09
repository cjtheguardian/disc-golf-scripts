package com.cj.theguardian.discgolf;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LayoutAnalysis {

    private Player player;

    private Map<String, HoleAnalysis> holeAnalyses;

    public LayoutAnalysis(Player player) {
        // TODO add course name and layout name to constructor so we can validate rounds being added are legit
        this.player = player;
        holeAnalyses = new HashMap<>();
    }

    public void addScore(HoleScore score) {
        HoleAnalysis analysis = holeAnalyses.computeIfAbsent(score.getHole(), key -> new HoleAnalysis());
        analysis.addScore(score);
    }

    public HoleAnalysis.HoleStats getStatsForHole(String hole) {
        return holeAnalyses.get(hole).getStats();
    }

    public void applyRound(Round round) {
        Score playersScore = round.getPlayers().stream().filter(s -> s.getPlayer().equals(player)).findFirst().orElse(null);
        if(playersScore == null) {
          //  System.out.println("not applying: " + round.getDate() + " : " + round.getLayout().getCourse().getName() + " : " + round.getLayout().getName());
            return;
        }

        Set<String> holes = playersScore.getHoleNames();
        for(String hole : holes) {
            int strokes = playersScore.getScoreForHole(hole);
            int par = round.getLayout().getParForHole(hole);
            addScore(new HoleScore(hole, par, strokes));
        }
    }
}
