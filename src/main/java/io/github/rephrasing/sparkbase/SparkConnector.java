package io.github.rephrasing.sparkbase;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


public class SparkConnector {
    private final MongoClient client;
    private SparkConnector(ConnectionString connectionString) {
        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder();
        settingsBuilder.applyConnectionString(connectionString);
        this.client = MongoClients.create(settingsBuilder.build());
        Sparkbase.logger.info("Successfully established connection to MongoDB");
    }

    static SparkConnector connect(String readyConnectionString) {
        return new SparkConnector(new ConnectionString(readyConnectionString));
    }

    MongoClient getClient() {
        return client;
    }
}
