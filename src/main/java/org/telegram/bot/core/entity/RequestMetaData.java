package org.telegram.bot.core.entity;

import org.telegram.bot.core.api.MethodType;

import java.util.Objects;

public class RequestMetaData {
    private final String state;
    private final String path;
    private MethodType type;

    public RequestMetaData getMatchingCondition(MethodType type, String state, String path) {
        if (type == this.type && path.matches(this.path) && state.matches(this.state)) return this;
        else return null;
    }

    public RequestMetaData(String state, String path, MethodType type) {
        this.state = state;
        this.path = path;
        this.type = type;
    }

    public static RequestMetaDataBuilder builder() {
        return new RequestMetaDataBuilder();
    }

    public static class RequestMetaDataBuilder {
        private String state;
        private String path;
        private MethodType type;

        public RequestMetaDataBuilder() {
        }

        public RequestMetaDataBuilder state(String state) {
            this.state = state;
            return this;
        }

        public RequestMetaDataBuilder path(String path) {
            this.path = path;
            return this;
        }

        public RequestMetaDataBuilder type(MethodType type) {
            this.type = type;
            return this;
        }

        public RequestMetaData build() {
            return new RequestMetaData(state, path, type);
        }

    }

    public String getState() {
        return state;
    }

    public String getPath() {
        return path;
    }

    public MethodType getType() {
        return type;
    }

    public void setType(MethodType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestMetaData that = (RequestMetaData) o;
        return Objects.equals(state, that.state) &&
                Objects.equals(path, that.path) &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, path, type);
    }
}