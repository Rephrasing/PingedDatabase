package io.github.rephrasing.sparkbase;

import com.mongodb.client.MongoCollection;
import io.github.rephrasing.sparkbase.exceptions.InitiationException;

import org.bson.Document;
public class Sparkbase {

    private final SparkConnector ratabaseConnector;
    static final SparkAdaptersHandler adaptersHandler = new SparkAdaptersHandler();
    static final SparkLogger logger = new SparkLogger();
    private static boolean initiated = false;

    private Sparkbase(SparkConnector ratabaseConnector) {
        this.ratabaseConnector = ratabaseConnector;
    }

    public static Sparkbase init(String mongodbConnectionString) {
        if (initiated) throw new IllegalArgumentException("Sparkbase is already initialized.");
        String password = mongodbConnectionString.split(":")[2].split("@")[0];
        if (password.equalsIgnoreCase("<password>")) {
            throw new InitiationException("You must provide a valid MongoDB Connection String -> (check if you have correctly replaced <password> with your own database password)");
        }
        adaptersHandler.init();
        initiated = true;
        return new Sparkbase(SparkConnector.connect(mongodbConnectionString));
    }

    static boolean isInitialized() {
        return initiated;
    }

    public SparkCollection getCollection(String databaseName, String collectionName) {
        MongoCollection<Document> raw = ratabaseConnector.getClient().getDatabase(databaseName).getCollection(collectionName);
        return new SparkCollection(raw);
    }

    public SparkAdaptersHandler getAdaptersHandler() {
        return adaptersHandler;
    }
}
