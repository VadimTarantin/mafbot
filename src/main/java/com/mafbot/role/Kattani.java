package com.mafbot.role;

public class Kattani extends BaseRole {
    public static final String ROLE_NAME = "комиссар Каттани";

    @Override
    public String getName() {
        return ROLE_NAME;
    }

    @Override
    public String getGreetingText() {
        return "Поклянись обществу и стань отважным комиссаром Каттани!";
    }
}
