package com.mafbot.role;

import com.mafbot.role.support.ProcessResult;
import com.mafbot.role.support.Target;

public class Kattani extends BaseRole {
    public static final String ROLE_NAME = "комиссар Каттани";

    @Override
    public String getName() {
        return ROLE_NAME;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public String getGreetingText() {
        return "Поклянись обществу и стань отважным комиссаром Каттани!";
    }

    @Override
    public String getTextAction() {
        return "Комиссар Каттани может проверить роль любого игрока. Выбери номер игрока из списка.";
    }

    @Override
    public ProcessResult doActionTo(Target target) {
        ProcessResult result = new ProcessResult();
        result.setTargetIsDied(false);
        String messageInCommonChannel = String.format("Комиссар Каттани не терял время и выяснил, кто на самом деле %s!",
                target.getName());
        result.setMessageAboutResultForCommonChannel(messageInCommonChannel);

        String messageToInitiator = String.format("По данным разведки, %s - это %s!",
                target.getName(), target.getName());
        result.setMessageAboutResultForOrdered(messageToInitiator);

        return result;
    }
}
