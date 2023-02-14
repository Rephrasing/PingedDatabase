package io.github.rephrasing.pinged;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;


public class PingedConnector {
    private final MongoClient client;
    private PingedConnector(ConnectionString connectionString) {
        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder();
        settingsBuilder.applyConnectionString(connectionString);
        this.client = MongoClients.create(settingsBuilder.build());
        PingedDatabase.logger.info("Successfully established connection to MongoDB");
    }

    static PingedConnector connect(String readyConnectionString) {
        return new PingedConnector(new ConnectionString(readyConnectionString));
    }

    MongoClient getClient() {
        return client;
    }
}
