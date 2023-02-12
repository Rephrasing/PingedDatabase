package io.github.rephrasing.sparkbase.adapters;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public interface SparkDataAdapter<T> extends JsonSerializer<T>, JsonDeserializer<T> {
    Class<T> getType();
}
