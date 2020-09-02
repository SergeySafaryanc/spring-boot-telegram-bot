package org.telegram.bot.core.entity;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

public class BeanMethod {

    private Object object;
    private Method method;

    public BeanMethod(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    public static BeanMethodBuilder builder() {
        return new BeanMethodBuilder();
    }

    public Object invoke(Update messageDate) throws IllegalAccessException, InvocationTargetException {

        if (Arrays.stream(method.getParameterTypes()).anyMatch(aClass -> aClass == Model.class)) {
            Model requestModel = new Model();
            return method.invoke(object, messageDate, requestModel);
        }
        return method.invoke(object, messageDate);
    }

    public static class BeanMethodBuilder {
        private Object object;
        private Method method;

        public BeanMethodBuilder() {
        }

        public BeanMethodBuilder object(Object object) {
            this.object = object;
            return this;
        }

        public BeanMethodBuilder method(Method method) {
            this.method = method;
            return this;
        }

        public BeanMethod build() {
            return new BeanMethod(object, method);
        }

    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BeanMethod that = (BeanMethod) o;
        return Objects.equals(object, that.object) &&
                Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object, method);
    }
}
