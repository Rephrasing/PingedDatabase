package io.github.rephrasing.sparkbase;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.rephrasing.sparkbase.adapters.SparkDataAdapter;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public class SparkAdaptersHandler {

    @Getter(value = AccessLevel.PACKAGE)
    private Gson gson;
    private final Set<SparkDataAdapter<?>> registeredAdapters = new HashSet<>();

    SparkAdaptersHandler() {}

    public void registerAdapter(SparkDataAdapter<?> adapter) {
        if (Sparkbase.isInitialized()) throw new IllegalArgumentException("Cannot register adapters after sparkbase has already been initiated.");
        registeredAdapters.add(adapter);
    }

    public void registerAdapters(SparkDataAdapter<?>... adapters) {
        if (Sparkbase.isInitialized()) throw new IllegalArgumentException("Cannot register adapters after sparkbase has already been initiated.");
        for (SparkDataAdapter<?> adapter : adapters) {
            registerAdapter(adapter);
        }
    }

    void init() {
        GsonBuilder builder = new GsonBuilder().serializeNulls();
        registeredAdapters.forEach(adapter -> builder.registerTypeAdapter(adapter.getType(), adapter));
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
