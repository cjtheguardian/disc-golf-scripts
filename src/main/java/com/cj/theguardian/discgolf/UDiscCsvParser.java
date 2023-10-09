package com.cj.theguardian.discgolf;

import com.cj.theguardian.utils.CsvReader;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class UDiscCsvParser {

    private Map<String, Course> courses = new HashMap<>();
    private List<Round> rounds = new ArrayList<>();


    public static List<Round> parseCsv(String classpath) throws Exception {
        CsvReader csvReader = new CsvReader(new File(classpath), true);
        UDiscCsvParser parser = new UDiscCsvParser();
        parser.init(csvReader);
        //Player.printUnregistered();)
        return parser.rounds;
    }

    private void init(CsvReader csvReader) {
        Map<String,String> nextRow = null;
        Round currentRound = null;

        while((nextRow = csvReader.nextRowAsMap()) != null) {
            if(nextRow.get("PlayerName").equals("Par")) {
                currentRound = setupRound(nextRow);
                rounds.add(currentRound);
            } else {
                addPlayerToRound(currentRound, nextRow);
            }
        }
        // TODO hack to exclude doubles rounds always (specifically exclude the doubles rounds where i am sven)
        rounds = rounds.stream().filter((r -> {
            List<Player> players = r.getPlayers().stream().map(s -> s.getPlayer()).collect(Collectors.toList());
            return !players.contains(Player.DOUBLES);
        })).collect(Collectors.toList());
        rounds.sort(Comparator.comparing(r -> r.getDate()));
    }

    private void addPlayerToRound(Round currentRound, Map<String, String> nextRow) {
        Player player = Player.fromName(nextRow.get("PlayerName"));
        Score score = new Score();
        score.setPlayer(player);
        score.setStrokes(Integer.parseInt(nextRow.get("Total")));
        int hole = 1;
        String key = "Hole" + hole;
        while(nextRow.containsKey(key)) {
            if(StringUtils.isNotBlank(nextRow.get(key))) {
                score.setScoreForHole("" + hole, Integer.parseInt(nextRow.get(key)));
            }
            hole++;
            key = "Hole" + hole;
        }
        currentRound.getPlayers().add(score);
    }

    private Round setupRound(Map<String, String> nextRow) {
        Round round = new Round();
        round.setDate(LocalDateTime.parse(nextRow.get("Date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        round.setLayout(new Layout());
        round.getLayout().setCourse(getCourse(nextRow.get("CourseName")));
        round.getLayout().setName(nextRow.get("LayoutName"));
        round.getLayout().setPar(Integer.parseInt(nextRow.get("Total")));
        round.setPlayers(new ArrayList<>());
        int hole = 1;
        String key = "Hole" + hole;
        while(nextRow.containsKey(key)) {
            if(StringUtils.isNotBlank(nextRow.get(key))) {
                round.getLayout().setParForHole(""+hole, Integer.parseInt(nextRow.get(key)));
            }
            hole++;
            key = "Hole" + hole;
        }
        return round;
    }

    private Course getCourse(String courseName) {
        return courses.computeIfAbsent(courseName, k -> {
            Course course = new Course();
            course.setName(k);
            return course;
        });
    }

}
