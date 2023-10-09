package com.cj.theguardian.discgolf;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Layout {

    private Course course;
    private String name;
    private int par;
    private Map<String, Integer> parPerHole = new HashMap<>();

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }

    public void setParForHole(String hole, Integer par) {
        parPerHole.put(hole, par);
    }

    public Integer getParForHole(String hole) {
        return parPerHole.get(hole);
    }

    public List<Hole> setupHoles() {
        Comparator<String> comparator = (n1, n2) -> {
           int i1 = Integer.parseInt(n1.replaceAll("[^\\d.]", ""));
           int i2 = Integer.parseInt(n2.replaceAll("[^\\d.]", ""));
           return i1 - i2;
        };
        List<String> orderedHoles = parPerHole.keySet().stream().sorted(comparator).collect(Collectors.toList());
        return orderedHoles.stream().map(name -> new Hole(name, parPerHole.get(name))).collect(Collectors.toList());
    }
}
