package com.mafbot.initialization;

import com.mafbot.message.outgoing.OutgoingSender;
import com.mafbot.message.outgoing.OutgoingSenderImpl;

public class BeanRepository {
    private static final BeanRepository INSTANCE = new BeanRepository();

    private OutgoingSender outgoingSender;

    private BeanRepository() {
        this.outgoingSender = new OutgoingSenderImpl();
    }

    public static BeanRepository getInstance() {
        return INSTANCE;
    }

    public OutgoingSender getOutgoingSender() {
        return outgoingSender;
    }
}
