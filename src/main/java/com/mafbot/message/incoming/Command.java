package com.mafbot.message.incoming;

import java.util.HashSet;
import java.util.Set;

public enum Command {
    START("/start", "Стартовать новую игру")
    ,START_ALT("/старт", "Стартовать новую игру")
    ,REG("/reg", "Зарегистрироваться в игру")
    ,REG_ALT("/рег", "Зарегистрироваться в игру")
    ,LIST("/list", "Показать список игроков")
    ,LIST_ALT("/лист", "Показать список игроков")
    ,ROLE("/role", "Показать свою роль")
    ,ROLE_ALT("/роль", "Показать свою роль")
    ;

    private static Set<String> availableCommands = createAvailableCommands();

    public String name;
    public String description;

    Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    private static Set<String> createAvailableCommands() {
        Set<String> result = new HashSet<>();
        Command[] values = values();
        for (Command value : values) {
            result.add(value.name);
        }
        return result;
    }

    public static boolean isCommandAvailable(String candidate) {
        return availableCommands.contains(candidate) || isDigit(candidate);
    }

    private static boolean isDigit(String candidate) {
        try {
            Integer.parseInt(candidate);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
