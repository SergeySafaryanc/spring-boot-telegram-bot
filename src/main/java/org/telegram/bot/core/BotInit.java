package org.telegram.bot.core;

import org.springframework.stereotype.Component;
import org.telegram.bot.core.config.BotConfig;
import org.telegram.bot.core.entity.BeanMethod;
import org.telegram.bot.core.handler.HandlerMethodMapping;
import org.telegram.bot.core.handler.MethodReturnedHandler;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotInit extends TelegramLongPollingBot {

    protected final HandlerMethodMapping handlerMethodMapping;
    protected final MethodReturnedHandler methodReturnedHandler;
    protected final BotConfig config;

    public BotInit(HandlerMethodMapping handlerMethodMapping, MethodReturnedHandler methodReturnedHandler, BotConfig config) {
        this.handlerMethodMapping = handlerMethodMapping;
        this.methodReturnedHandler = methodReturnedHandler;
        this.config = config;
    }


    static {
        ApiContextInitializer.init();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            BeanMethod beanMethod = handlerMethodMapping.resolveHandlingMethod(update);
            Object methodResult = beanMethod.invoke(update);
            BotApiMethod<?> resultMethod = methodReturnedHandler.handleResult(update, methodResult);
            sendApiMethod(resultMethod);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return config.getName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
