package com.mafbot.role;

public class Citizen implements Role {
    public static final String ROLE_NAME = "Мирный житель";

    @Override
    public String getName() {
        return ROLE_NAME;
    }

    @Override
    public String getGreetingText() {
        return "В этой игре вы - мирный житель!";
    }
}