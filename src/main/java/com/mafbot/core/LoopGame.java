package com.mafbot.core;

import com.mafbot.initialization.BeanRepository;
import com.mafbot.message.outgoing.OutgoingSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoopGame extends Thread {
    private static final Logger log = LogManager.getLogger(LoopGame.class);
    private OutgoingSender outgoingSender;

    public LoopGame() {
        this.outgoingSender = BeanRepository.getInstance().getOutgoingSender();
    }

    /**
     * После старта бота этот метод должен постоянно работать, делать необходимые проверки, отсылать все необходимые сообщения и т.п.
     */
    @Override
    public void run() {
        log.info("Успешно запущен основной цикл '{}'", getName());

        while (!isInterrupted()) {
            try {
                Thread.sleep(60000L);
                outgoingSender.sendInCommonChannel("Общее сообщение по таймеру");;
            } catch (InterruptedException e) {
                log.warn("Основной цикл прерван!");
                break;
            }
        }
        log.info("Завершение основного цикла");
    }
}