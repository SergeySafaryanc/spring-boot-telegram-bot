package org.telegram.bot.core.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BotMapping {
    String state() default "";
    String path() default ".*";
    MethodType type() default MethodType.MESSAGE;
}
