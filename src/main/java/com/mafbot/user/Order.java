package com.mafbot.user;

public class Order {
    private Integer minNumberPotentialTarget = 1;
    private Integer maxNumberPotentialTarget;
    private Integer target;
    private boolean sentOrderToPlayer;

    public Order(Integer maxNumberPotentialTarget) {
        this.maxNumberPotentialTarget = maxNumberPotentialTarget;
        sentOrderToPlayer = true;
    }

    public Integer getMinNumberPotentialTarget() {
        return minNumberPotentialTarget;
    }

    public Integer getMaxNumberPotentialTarget() {
        return maxNumberPotentialTarget;
    }

    public Integer getTarget() {
        return target;
    }

    public boolean wasOrder() {
        return target != null;
    }

    public void setTarget(Integer target) {
        this.target = target;
    }

    public boolean isSentOrderToPlayer() {
        return sentOrderToPlayer;
    }

    public void setSentOrderToPlayer(boolean sentOrderToPlayer) {
        this.sentOrderToPlayer = sentOrderToPlayer;
    }
}
