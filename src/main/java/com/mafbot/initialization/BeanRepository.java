package com.mafbot.initialization;

import com.mafbot.core.GameManager;
import com.mafbot.message.incoming.IncomingMessageManager;
import com.mafbot.message.incoming.IncomingMessageManagerImpl;
import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.message.outgoing.OutgoingSenderImpl;
import com.mafbot.statistic.StatisticServiceImpl;
import com.mafbot.statistic.StatisticsService;

public class BeanRepository {
    private static final BeanRepository INSTANCE = new BeanRepository();

    private OutgoingSender outgoingSender;
    private IncomingMessageManager incomingMessageManager;
    private GameManager gameManager;
    private StatisticsService statisticsService;

    private BeanRepository() {
        outgoingSender = new OutgoingSenderImpl();
        incomingMessageManager = new IncomingMessageManagerImpl();
        incomingMessageManager.setOutgoingSender(outgoingSender);
        gameManager = new GameManager();
        gameManager.setOutgoingSender(outgoingSender);
        statisticsService = new StatisticServiceImpl();
    }

    public static BeanRepository getInstance() {
        return INSTANCE;
    }

    public OutgoingSender getOutgoingSender() {
        return outgoingSender;
    }

    public IncomingMessageManager getIncomingMessageManager() {
        return incomingMessageManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public StatisticsService getStatisticsService() {
        return statisticsService;
    }
}
