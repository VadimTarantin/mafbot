package com.mafbot.statistic;

public class StatisticServiceImpl implements StatisticsService {

    private int gameNumber;

    @Override
    public int getNewGameNumber() {
        return ++gameNumber;
    }

    @Override
    public int getGameNumber() {
        return gameNumber;
    }
}