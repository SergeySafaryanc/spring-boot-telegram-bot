package org.telegram.bot.core.handler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.telegram.bot.core.api.BotController;
import org.telegram.bot.core.api.BotMapping;
import org.telegram.bot.core.api.MethodType;
import org.telegram.bot.core.cache.State;
import org.telegram.bot.core.entity.BeanMethod;
import org.telegram.bot.core.entity.RequestMetaData;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;

@Component
public class HandlerMethodMapping implements InitializingBean, ApplicationContextAware {

    private ApplicationContext context;
    private final HashMap<RequestMetaData, BeanMethod> requestMethodMap = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        context.getBeansWithAnnotation(BotController.class).values().forEach(controller -> {
            Arrays.stream(controller.getClass().getMethods())
                    .filter(method -> method.isAnnotationPresent(BotMapping.class))
                    .forEach(method -> {
                        BotMapping botMapping = method.getAnnotation(BotMapping.class);
                        if (method.getParameters()[0].getType() != Update.class) {
                            throw new RuntimeException(method.getName());
                        }
                        RequestMetaData requestMetaData = RequestMetaData.builder()
                                .path(botMapping.path())
                                .state(botMapping.state())
                                .type(botMapping.type())
                                .build();
                        BeanMethod beanMethod = BeanMethod.builder()
                                .object(controller)
                                .method(method)
                                .build();
                        requestMethodMap.put(requestMetaData, beanMethod);
                    });
        });
    }

    @Override
    public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public BeanMethod resolveHandlingMethod(Update update) {
        MethodType methodType = null;
        String path = "";
        String state = "";

        if (update.hasCallbackQuery()) {
            methodType = MethodType.CALLBACK;
            path = update.getCallbackQuery().getData();
        } else if (update.getMessage().isReply()) {
            methodType = MethodType.REPLY;
            path = update.getMessage().getReplyToMessage().getText();
        } else if (update.getMessage().hasText()){
            methodType = MethodType.MESSAGE;
            path = update.getMessage().getText();
            state = State.getState(update.getMessage().getChatId());
        }

        for (RequestMetaData requestMetaData: requestMethodMap.keySet()) {
            if (requestMetaData.getMatchingCondition(methodType, state, path) != null) {
                return requestMethodMap.get(requestMetaData);
            }
        }
        return null;
    }
}