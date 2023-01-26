package io.github.rephrasing.sparkbase.adapters;

import io.github.rephrasing.sparkbase.exceptions.EntryNotFoundException;
import org.bson.Document;

public interface SparkDataAdapter<T> {

    Document serialize(T object);
    T deserialize(Document document) throws EntryNotFoundException;
    Class<T> getType();


}
