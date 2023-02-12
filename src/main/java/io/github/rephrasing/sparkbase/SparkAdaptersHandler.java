package io.github.rephrasing.sparkbase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.rephrasing.sparkbase.adapters.SparkDataAdapter;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.Arrays;

public class SparkAdaptersHandler {

    @Getter(value = AccessLevel.PACKAGE)
    private final Gson gson;

    SparkAdaptersHandler() {
        this.gson = new GsonBuilder().serializeNulls().create();
    }
    SparkAdaptersHandler(SparkDataAdapter<?>... sparkDataAdapters) {
        GsonBuilder builder = new GsonBuilder().serializeNulls();
        Arrays.asList(sparkDataAdapters).forEach(adapter -> builder.registerTypeAdapter(adapter.getType(), adapter));
        this.gson = builder.create();
    }

    boolean isSerializableType(Class<?> clazz) {
        try {
            gson.getAdapter(clazz);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }


    <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    <T> String toJson(T instance) {
        return gson.toJson(instance);
    }
}
