package org.telegram.bot.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BotConfig {
    @Value("${telegram.bot.token}")
    private String token;
    @Value("${telegram.bot.name}")
    private String name;

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }
}
