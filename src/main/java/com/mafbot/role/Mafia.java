package com.mafbot.role;

public class Mafia extends BaseRole {
    public static final String ROLE_NAME = "Мафиози";
    private int mafNumber;

    public Mafia(int mafNumber) {
        this.mafNumber = mafNumber;
    }

    @Override
    public String getName() {
        return ROLE_NAME + " " + mafNumber;
    }

    @Override
    public String getGreetingText() {
        return "Теперь ты будешь кидать народ на бабки, ты - злобный мафиози!";
    }
}