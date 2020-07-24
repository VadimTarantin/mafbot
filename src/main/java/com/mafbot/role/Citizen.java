package com.mafbot.role;

import com.mafbot.role.support.ProcessResult;
import com.mafbot.role.support.Target;

public class Citizen extends BaseRole {
    public static final String ROLE_NAME = "Мирный житель";

    @Override
    public String getName() {
        return ROLE_NAME;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public String getGreetingText() {
        return "В этой игре вы - мирный житель!";
    }

    @Override
    public String getTextAction() {
        throw new UnsupportedOperationException("Мирные жители не могут ходить ночью!");
    }

    @Override
    public ProcessResult doActionTo(Target target) {
        throw new UnsupportedOperationException("Мирные жители не могут никому ничего сделать!");
    }
}