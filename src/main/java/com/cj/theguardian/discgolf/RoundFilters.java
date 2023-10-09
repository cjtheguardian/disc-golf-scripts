package com.cj.theguardian.discgolf;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RoundFilters {

    public static Predicate<Round> holeCount(int min) {
        return r -> r.totalHoles() >= min;
    }

    public static Predicate<Round> inTheLastXMonths(int months) {
        return since(LocalDateTime.now().minusMonths(months));
    }

    public static Predicate<Round> sinceTheLastYear() {
        return since(LocalDateTime.now().minusYears(1));
    }


    public static Predicate<Round> byYear(int year) {
        return since(LocalDateTime.of(year, 1, 1,0,0 )).and(before(LocalDateTime.of(year+1, 1,1,0,0)));
    }

    public static Predicate<Round> since(LocalDateTime date) {
        return r -> r.getDate().isAfter(date) || r.getDate().equals(date);
    }
    public static Predicate<Round> before(LocalDateTime date) {
        return r -> r.getDate().isBefore(date);
    }

    public static List<Round> applyFilters(List<Round> rounds, Predicate<Round>... filters) {
        Stream<Round> stream = rounds.stream();
        for(Predicate<Round> filter : filters) {
            stream = stream.filter(filter);
        }
        return stream.collect(Collectors.toList());
    }

    public static Predicate<Round> casitas() {
        return r -> r.getLayout().getCourse().getName().startsWith("Coyote") && r.getLayout().getName().equalsIgnoreCase("Main");
    }


    public static Predicate<Round> hasPlayers(Player... players) {
        return r -> {
          List<Player> playerNames = r.getPlayers().stream().map(p -> p.getPlayer()).collect(Collectors.toList());
          for(Player player : players) {
              if(!playerNames.contains(player)) {
                  return false;
              }
          }
          return true;
        };
    }

    public static Predicate<Round> sinceConorGotGood() {
        return since(LocalDateTime.parse("2022-07-05T00:00:00"));
    }


    public static Predicate<Round> sincePatGotGood() {
        return since(LocalDateTime.parse("2022-10-07T00:00:00"));
    }

    public static Predicate<Round> wolfHill() {
        return r -> r.getLayout().getCourse().getName().startsWith("Wolf Hill") && r.getLayout().getName().equalsIgnoreCase("Normal Course");
    }
}
