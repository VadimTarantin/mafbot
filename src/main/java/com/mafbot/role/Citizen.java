package com.mafbot.role;

public class Citizen extends BaseRole {
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