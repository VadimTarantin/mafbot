package com.mafbot.initialization;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.util.Properties;

public class MafTelegramBotPropertiesHolder {
    private static final Logger log = LogManager.getLogger(MafTelegramBotPropertiesHolder.class);

    private static final MafTelegramBotPropertiesHolder INSTANCE = new MafTelegramBotPropertiesHolder();

    private static final String APPLICATION_PROPERTIES_NAME = "application.properties";
    private static final String BOT_NAME_KEY = "bot.name";
    private static final String BOT_TOKEN_KEY = "bot.token";
    private static final String CHANNEL_ID_COMMON_KEY = "channel.id.common";
    private static final String CHANNEL_ID_COMMON_KEY = "channel.id.common";
    private final Properties prop = new Properties();

    private MafTelegramBotPropertiesHolder() {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES_NAME);
            prop.load(is);
            StringBuilder sb = new StringBuilder();
            sb.append("Настройки бота успешно загружены из '");
            sb.append(APPLICATION_PROPERTIES_NAME);
            sb.append("':");
            sb.append(System.getProperty("line.separator"));
            for (Object key : prop.keySet()) {
                sb.append(key);
                sb.append("->");
                sb.append(prop.get(key));
                sb.append(System.getProperty("line.separator"));
            }
            log.info(sb.toString());
        } catch (Exception e) {
            System.out.println("Не удалось загрузить настройки бота из '" + APPLICATION_PROPERTIES_NAME +"'");
            log.error("Не удалось загрузить настройки бота из '" + APPLICATION_PROPERTIES_NAME +"'");
        }
    }

    public static MafTelegramBotPropertiesHolder getInstance() {
        return INSTANCE;
    }

    public String getBotName() {
        return prop.getProperty(BOT_NAME_KEY);
    }

    public String getBotToken() {
        return prop.getProperty(BOT_TOKEN_KEY);
    }

    public String getChannelIdCommon() {
        return prop.getProperty(CHANNEL_ID_COMMON_KEY);
    }
}
