package com.mafbot.role;

import com.mafbot.role.support.ProcessResult;
import com.mafbot.role.support.Target;

public class Mafia extends BaseRole {
    public static final String ROLE_NAME = "Мафиози";
    private int mafNumber;

    public Mafia(int mafNumber) {
        this.mafNumber = mafNumber;
    }

    @Override
    public boolean isActive() {
        return mafNumber == 1;
    }

    @Override
    public String getName() {
        return ROLE_NAME + " " + mafNumber;
    }

    @Override
    public String getGreetingText() {
        return "Теперь ты будешь кидать народ на бабки, ты - злобный мафиози!";
    }

    @Override
    public String getTextAction() {
        return "Пора мочить людишек! Выбирай номер из списка.";
    }

    @Override
    public ProcessResult doActionTo(Target target) {
        ProcessResult result = new ProcessResult();
        result.setTargetIsDied(true);

        String messageInCommonChannel = String.format("Мафия в этот раз решила пристрелить ни в чем ни повинного %s %s!",
                target.getRole().getName(), target.getName());
        result.setMessageAboutResultForCommonChannel(messageInCommonChannel);
        return result;
    }
}