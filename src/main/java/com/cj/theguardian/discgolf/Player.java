package com.cj.theguardian.discgolf;

import java.util.HashSet;
import java.util.Set;

public enum Player {

    CONOR("Cjtheguardian", "Conor Fallon"),
    PAT("Patchwah"),
    DOUBLES,
    OTHER;

    private static Set<String> unregisteredNames = new HashSet<>();
    private Set<String> names;

    private Player(String... aliases) {
        names = new HashSet<>();
        for(String alias : aliases) {
            names.add(alias);
        }
    }

    public static Player fromName(String udiscName) {
        for(Player player : values()) {
            if(player.names.contains(udiscName)) {
                return player;
            }
        }
        if(udiscName.contains(" + ")) {
            return DOUBLES;
        }
        unregisteredNames.add(udiscName);
        return OTHER;
    }

    public static void printUnregistered() {
        System.out.println("Unregistered players:");
        for(String player : unregisteredNames) {
            System.out.println("\t"+player);
        }
    }

}
