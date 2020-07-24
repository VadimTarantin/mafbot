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
        String message = "Для старта новой игры наберите /старт. Для регистрации в игре /рег." +
                " Список игроков: /лист. Узнать свою роль: /роль";
        log.info("Успешно запущен основной цикл '{}'", getName());
        while (!isInterrupted()) {
            try {
                outgoingSender.sendInCommonChannel(message);
                log.info(message);
                Thread.sleep(60000L);
            } catch (InterruptedException e) {
                log.warn("Основной цикл прерван!", e);
                interrupt();
                break;
            }
        }
        log.info("Завершение основного цикла");
    }
}