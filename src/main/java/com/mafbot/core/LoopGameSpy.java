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
        log.info("Успешно запущен основной цикл '{}'", getName());
        outgoingSender.sendInCommonChannel("Бот находится в режиме ожидания. Для старта новой игры наберите /рег");

        while (!isInterrupted()) {
            try {
                Thread.sleep(60000L);
//                outgoingSender.sendInCommonChannel("Общее сообщение по таймеру");
                log.info("Общее сообщение по таймеру логирование");
            } catch (InterruptedException e) {
                log.warn("Основной цикл прерван!", e);
                interrupt();
                break;
            }
        }
        log.info("Завершение основного цикла");
    }
}