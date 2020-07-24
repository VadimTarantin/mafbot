package com.mafbot.role.support;

public class ProcessResult {
    private boolean targetIsDied;
    private String messageAboutResultForCommonChannel;
    private String messageAboutResultForOrdered;

    public boolean isTargetIsDied() {
        return targetIsDied;
    }

    public void setTargetIsDied(boolean targetIsDied) {
        this.targetIsDied = targetIsDied;
    }

    public boolean existMessageAboutResultForOrdered() {
        return messageAboutResultForOrdered != null;
    }

    public String getMessageAboutResultForCommonChannel() {
        return messageAboutResultForCommonChannel;
    }

    public void setMessageAboutResultForCommonChannel(String messageAboutResultForCommonChannel) {
        this.messageAboutResultForCommonChannel = messageAboutResultForCommonChannel;
    }

    public String getMessageAboutResultForOrdered() {
        return messageAboutResultForOrdered;
    }

    public void setMessageAboutResultForOrdered(String messageAboutResultForOrdered) {
        this.messageAboutResultForOrdered = messageAboutResultForOrdered;
    }
}
