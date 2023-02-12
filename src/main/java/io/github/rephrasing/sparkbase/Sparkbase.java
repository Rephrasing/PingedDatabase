package io.github.rephrasing.sparkbase;

import com.mongodb.client.MongoCollection;
import io.github.rephrasing.sparkbase.adapters.SparkDataAdapter;
import io.github.rephrasing.sparkbase.exceptions.InitiationException;

import org.bson.Document;


public class Sparkbase {

    private final SparkConnector ratabaseConnector;
    static SparkAdaptersHandler adaptersHandler;
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
        adaptersHandler = new SparkAdaptersHandler();
        initiated = true;
        return new Sparkbase(SparkConnector.connect(mongodbConnectionString));
    }

    public static Sparkbase init(String mongodbConnectionString, SparkDataAdapter<?>... sparkDataAdapters) {
        if (initiated) throw new IllegalArgumentException("Sparkbase is already initialized.");
        String password = mongodbConnectionString.split(":")[2].split("@")[0];
        if (password.equalsIgnoreCase("<password>")) {
            throw new InitiationException("You must provide a valid MongoDB Connection String -> (check if you have correctly replaced <password> with your own database password)");
        }
        adaptersHandler = new SparkAdaptersHandler(sparkDataAdapters);
        initiated = true;
        return new Sparkbase(SparkConnector.connect(mongodbConnectionString));
    }

    public SparkCollection getCollection(String databaseName, String collectionName) {
        MongoCollection<Document> raw = ratabaseConnector.getClient().getDatabase(databaseName).getCollection(collectionName);
        return new SparkCollection(raw);
    }
}
