package com.mafbot.role;

public abstract class BaseRole implements Role {
    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getSkipTurnTest() {
        return getName() + " решил этой ночью выспаться и пропустил ход!";
    }
}