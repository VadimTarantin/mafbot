package com.mafbot.core;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.message.outgoing.OutgoingSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoopGameSpy extends Thread {
    private static final Logger log = LogManager.getLogger(LoopGameSpy.class);
    private OutgoingSender outgoingSender;

    public LoopGameSpy() {
        this.outgoingSender = BeanRepository.getInstance().getOutgoingSender();
    }

    /**
     * После старта бота этот метод должен постоянно работать, делать необходимые проверки, отсылать все необходимые сообщения и т.п.
     * Это такой следящий за всем поток.
     */
    @Override
    public void run() {
        String message = "Для старта новой игры наберите /старт или /start. Для регистрации в игре /рег или /reg." +
                " Список игроков: /лист или /list. Узнать свою роль: /роль или /role";
        log.info("Успешно запущен следящий поток '{}'", getName());
        outgoingSender.sendInCommonChannel(message);
        log.info("Завершение следящего потока");
    }
}