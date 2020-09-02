package org.telegram.bot.core.entity;

import java.util.HashMap;
import java.util.Objects;

public class Model {

    private HashMap<String, String> modelMap;

    public void put(String key, String value) {
        modelMap.put(key, value);
    }

    public Model() {
    }

    public HashMap<String, String> getModelMap() {
        return modelMap;
    }

    public void setModelMap(HashMap<String, String> modelMap) {
        this.modelMap = modelMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return Objects.equals(modelMap, model.modelMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelMap);
    }

    @Override
    public String toString() {
        return "Model{" +
                "modelMap=" + modelMap +
                '}';
    }
}
