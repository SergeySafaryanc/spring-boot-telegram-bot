package org.telegram.bot.core.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MethodReturnedHandler {

    public BotApiMethod<?> handleResult(Update update, Object methodResult) {
        if (methodResult instanceof BotApiMethod) {
            return (BotApiMethod<?>) methodResult;
        } else if (methodResult instanceof String) {
            return new SendMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setText((String) methodResult);
        }
        return null;
    }

}
