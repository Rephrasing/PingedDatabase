package io.github.rephrasing.pinged.adapters;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface PingedDataAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {
    Class<T> getType();
}
