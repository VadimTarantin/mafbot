package com.mafbot.role;

public abstract class BaseRole implements Role {
    @Override
    public String toString() {
        return getName();
    }
}