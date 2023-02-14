package io.github.rephrasing.pinged;

import com.mongodb.client.MongoCollection;
import io.github.rephrasing.pinged.adapters.PingedDataAdapter;
import io.github.rephrasing.pinged.exceptions.InitiationException;

import io.github.rephrasing.pinged.pinging.PingedProcess;
import org.bson.Document;

import java.util.concurrent.TimeUnit;


public class PingedDatabase {

    private final PingedConnector ratabaseConnector;
    static PingedAdaptersHandler adaptersHandler;
    static final PingedLogger logger = new PingedLogger();
    private static boolean initiated = false;

    private PingedDatabase(PingedConnector ratabaseConnector) {
        this.ratabaseConnector = ratabaseConnector;
    }

    public static PingedDatabase init(String mongodbConnectionString) {
        PingedProcess ping = new PingedProcess();
        if (initiated) throw new IllegalArgumentException("PingedDatabase is already initialized.");
        String password = mongodbConnectionString.split(":")[2].split("@")[0];
        if (password.equalsIgnoreCase("<password>")) {
            throw new InitiationException("You must provide a valid MongoDB Connection String -> (check if you have correctly replaced <password> with your own database password)");
        }
        adaptersHandler = new PingedAdaptersHandler();
        initiated = true;
        long timeTaskTook = ping.end(TimeUnit.SECONDS);
        logger.formatInfo("Initializing took %s seconds!", timeTaskTook);
        return new PingedDatabase(PingedConnector.connect(mongodbConnectionString));
    }

    public static PingedDatabase init(String mongodbConnectionString, PingedDataAdapter<?>... sparkDataAdapters) {
        PingedProcess ping = new PingedProcess();
        if (initiated) throw new IllegalArgumentException("PingedDatabase is already initialized.");
        String password = mongodbConnectionString.split(":")[2].split("@")[0];
        if (password.equalsIgnoreCase("<password>")) {
            throw new InitiationException("You must provide a valid MongoDB Connection String -> (check if you have correctly replaced <password> with your own database password)");
        }
        adaptersHandler = new PingedAdaptersHandler(sparkDataAdapters);
        initiated = true;
        long timeTaskTook = ping.end(TimeUnit.SECONDS);
        logger.formatInfo("Initializing took %s seconds!", timeTaskTook);
        return new PingedDatabase(PingedConnector.connect(mongodbConnectionString));
    }

    public PingedCollection getCollection(String databaseName, String collectionName) {
        MongoCollection<Document> raw = ratabaseConnector.getClient().getDatabase(databaseName).getCollection(collectionName);
        return new PingedCollection(raw);
    }
}
