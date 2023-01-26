package io.github.rephrasing.sparkbase;

import com.mongodb.client.MongoCollection;
import io.github.rephrasing.sparkbase.adapters.SparkAdaptersHandler;
import io.github.rephrasing.sparkbase.exceptions.InitiationException;
import io.github.rephrasing.sparkbase.exceptions.NoConnectionFoundException;
import io.github.rephrasing.sparkbase.wrappers.SparkCollection;

import org.bson.Document;

import java.util.logging.Logger;

public class Sparkbase {

    static final Logger rawLogger = Logger.getLogger("SparkBase");
    private final SparkConnector ratabaseConnector;
    private static Sparkbase instance;
    static final SparkLogger logger = new SparkLogger();


    private Sparkbase(SparkConnector ratabaseConnector) {
        this.ratabaseConnector = ratabaseConnector;
    }


    /**
     * Initiates Ratabase and connects to your own mongodb database
     * @param mongodbConnectionString the connection string
     * @return the Ratabase instance
     * @see Sparkbase#getInstance()
     */
    public static Sparkbase init(String mongodbConnectionString) {
        String password = mongodbConnectionString.split(":")[2].split("@")[0];
        if (password.equalsIgnoreCase("<password>")) {
            throw new InitiationException("You must provide a valid MongoDB Connection String -> (check if you have correctly replaced <password> with your own database password)");
        }
        instance = new Sparkbase(SparkConnector.connect(mongodbConnectionString));
        return instance;
    }

    /**
     * Retrieves {@link Sparkbase} instance
     * @return the instance
     * @throws NoConnectionFoundException> When attempting to call this method before calling {@link Sparkbase#init(String)}
     * @see Sparkbase#init(String)
     */
    public static Sparkbase getInstance() {
        if (instance == null) throw new NoConnectionFoundException("You must init Sparkbase before attempting to use it.");
        return instance;
    }

    /**
     * Retrieves a {@link SparkCollection} instance
     * @param databaseName the mongodb database name
     * @param collectionName the mongodb collection name
     * @return a {@link SparkCollection}, mongodb will create a new instance if no instances were found
     */
    public SparkCollection getCollection(String databaseName, String collectionName) {
        MongoCollection<Document> raw = ratabaseConnector.getClient().getDatabase(databaseName).getCollection(collectionName);
        return new SparkCollection(raw);
    }

    public SparkAdaptersHandler getAdaptersHandler() {
        return SparkAdaptersHandler.getInstance();
    }
}
