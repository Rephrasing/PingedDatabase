package io.github.rephrasing.pinged;

import com.mongodb.client.MongoCollection;

import io.github.rephrasing.pinged.pinging.PingedProcess;
import lombok.SneakyThrows;
import org.bson.Document;

import javax.annotation.CheckReturnValue;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.github.rephrasing.pinged.PingedDatabase.logger;

public class PingedCollection {

    private final MongoCollection<Document> raw;

    public PingedCollection(MongoCollection<Document> raw) {
        this.raw = raw;
    }

    @CheckReturnValue
    public <T> void push(T instance, Class<T> type) {
        PingedProcess ping = new PingedProcess();
        PingedAdaptersHandler handler = PingedDatabase.adaptersHandler;
        boolean isSerializable = handler.isSerializableType(type);
        if (!isSerializable)
            throw new IllegalArgumentException("Class type \"" + type.getSimpleName() + "\" does not have a SparkDataAdapter. cannot be serialized.");
        Document document = Document.parse(handler.toJson(instance));
        raw.insertOne(document);
        logger.formatInfo("Pushing took %s seconds!", ping.end(TimeUnit.SECONDS));
    }

    @CheckReturnValue
    public <T> void pushOrReplace(T instance, Class<T> type, Predicate<T> filter) {
        PingedProcess ping = new PingedProcess();
        PingedAdaptersHandler handler = PingedDatabase.adaptersHandler;
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
        logger.formatInfo("Pushing took %s seconds!", ping.end(TimeUnit.SECONDS));
    }

    @CheckReturnValue
    public <T> Optional<T> pull(Class<T> type, Predicate<T> filter) {
        PingedProcess ping = new PingedProcess();
        PingedAdaptersHandler handler = PingedDatabase.adaptersHandler;
        boolean isTypeSerializable = handler.isSerializableType(type);
        if (!isTypeSerializable)
            throw new IllegalArgumentException("Class type \"" + type.getSimpleName() + "\" does not have a SparkDataAdapter. Cannot be deserialized.");
        if (raw.countDocuments() != 0) {
            for (Document document : raw.find()) {
                T deserialized = handler.fromJson(document.toJson(), type);
                if (filter.test(deserialized)) {
                    logger.formatInfo("Pulling took %s seconds!", ping.end(TimeUnit.SECONDS));
                    return Optional.of(deserialized);
                }
            }
        }
        return Optional.empty();
    }

    @CheckReturnValue
    public <T> boolean drop(Predicate<T> filter, Class<T> type) {
        PingedProcess ping = new PingedProcess();
        PingedAdaptersHandler handler = PingedDatabase.adaptersHandler;
        boolean isTypeSerializable = handler.isSerializableType(type);
        if (!isTypeSerializable)
            throw new IllegalArgumentException("Class type \"" + type.getSimpleName() + "\" does not have a SparkDataAdapter. Cannot be executed.");
        if (raw.countDocuments() != 0) {
            for (Document document : raw.find()) {
                if (filter.test(handler.fromJson(document.toJson(), type))) {
                    logger.formatInfo("Dropping took %s seconds!", ping.end(TimeUnit.SECONDS));
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
        PingedProcess ping = new PingedProcess();
        Optional<T> optionalT = pull(type, filter);
        if (optionalT.isPresent()) {
            // is found
            executeIfPresent.accept(optionalT.get());
            pushOrReplace(optionalT.get(), type, filter);
            logger.formatInfo("Execution took %s seconds!", ping.end(TimeUnit.SECONDS));
            return true;
        }
        // is not found
        pushOrReplace(executeIfNotFound.get(), type, filter);
        logger.formatInfo("Execution took %s seconds!", ping.end(TimeUnit.SECONDS));
        return false;
    }

}
