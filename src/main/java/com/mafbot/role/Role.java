package com.mafbot.role;

import com.mafbot.role.support.ProcessResult;
import com.mafbot.role.support.Target;

public interface Role {
    String getName();
    String getGreetingText();
    boolean isActive();
    String getTextAction();
    ProcessResult doActionTo(Target target);
    String getSkipTurnTest();
}
