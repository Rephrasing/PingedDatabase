package io.github.rephrasing.sparkbase;

import com.mongodb.client.MongoCollection;

import lombok.SneakyThrows;
import org.bson.Document;

import javax.annotation.CheckReturnValue;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SparkCollection {

    private final MongoCollection<Document> raw;

    public SparkCollection(MongoCollection<Document> raw) {
        this.raw = raw;
    }

    @CheckReturnValue
    public <T> void push(T instance, Class<T> type) {
        SparkAdaptersHandler handler = Sparkbase.adaptersHandler;
        boolean isSerializable = handler.isSerializableType(type);
        if (!isSerializable)
            throw new IllegalArgumentException("Class type \"" + type.getSimpleName() + "\" does not have a SparkDataAdapter. cannot be serialized.");
        Document document = Document.parse(handler.toJson(instance));
        raw.insertOne(document);
    }

    @CheckReturnValue
    public <T> void pushOrReplace(T instance, Class<T> type, Predicate<T> filter) {
        SparkAdaptersHandler handler = Sparkbase.adaptersHandler;
        boolean isTypeSerializable = handler.isSerializableType(type);
        if (!isTypeSerializable)
            throw new IllegalArgumentException("Class type \"" + type.getSimpleName() + "\" does not have a SparkDataAdapter. cannot be serialized.");
        Document document = Document.parse(handler.toJson(instance));
        if (raw.countDocuments() != 0) {
            for (Document doc : raw.find()) {
                T deserialized = handler.fromJson(doc.toJson(), type);
                if (filter.test(deserialized)) raw.deleteOne(doc);
            }
        }
        raw.insertOne(document);
    }

    @CheckReturnValue
    public <T> Optional<T> pull(Class<T> type, Predicate<T> filter) {
        SparkAdaptersHandler handler = Sparkbase.adaptersHandler;
        boolean isTypeSerializable = handler.isSerializableType(type);
        if (!isTypeSerializable)
            throw new IllegalArgumentException("Class type \"" + type.getSimpleName() + "\" does not have a SparkDataAdapter. Cannot be deserialized.");
        if (raw.countDocuments() != 0) {
            for (Document document : raw.find()) {
                T deserialized = handler.fromJson(document.toJson(), type);
                if (filter.test(deserialized)) return Optional.of(deserialized);
            }
        }
        return Optional.empty();
    }

    @CheckReturnValue
    public <T> boolean drop(Predicate<T> filter, Class<T> type) {
        SparkAdaptersHandler handler = Sparkbase.adaptersHandler;
        boolean isTypeSerializable = handler.isSerializableType(type);
        if (!isTypeSerializable)
            throw new IllegalArgumentException("Class type \"" + type.getSimpleName() + "\" does not have a SparkDataAdapter. Cannot be executed.");
        if (raw.countDocuments() != 0) {
            for (Document document : raw.find()) {
                if (filter.test(handler.fromJson(document.toJson(), type))) {
                    raw.findOneAndDelete(document);
                    return true;
                }
            }
        }
        return false;
    }

    @SneakyThrows
    @CheckReturnValue
    public <T> boolean ifPresentOrElse(Class<T> type, Predicate<T> filter, Consumer<T> executeIfPresent, Supplier<T> executeIfNotFound) {
        Optional<T> optionalT = pull(type, filter);
        if (optionalT.isPresent()) {
            // is found
            executeIfPresent.accept(optionalT.get());
            pushOrReplace(optionalT.get(), type, filter);
            return true;
        }
        // is not found
        pushOrReplace(executeIfNotFound.get(), type, filter);
        return false;
    }

}
