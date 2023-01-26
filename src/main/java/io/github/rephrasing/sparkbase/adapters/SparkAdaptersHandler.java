package io.github.rephrasing.sparkbase.adapters;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SparkAdaptersHandler {

    private static SparkAdaptersHandler instance;
    private final Set<SparkDataAdapter> registeredAdapters = new HashSet<>();

    private SparkAdaptersHandler() {}

    public static SparkAdaptersHandler getInstance() {
        if (instance == null) instance = new SparkAdaptersHandler();
        return instance;
    }

    /**
     * Registers a {@link SparkDataAdapter}
     * @param adapter the adapter
     */
    public void registerAdapter(SparkDataAdapter adapter) {
        registeredAdapters.add(adapter);
    }

    /**
     * Registers adapters one by one
     * @param adapters the adapters
     */
    public void registerAdapters(SparkDataAdapter... adapters) {
        for (SparkDataAdapter adapter : adapters) {
            registerAdapter(adapter);
        }
    }

    /**
     * Retrieves a {@link SparkDataAdapter}
     * @param type the type of the adapter
     * @return the adapter
     * @param <T> the class type
     */
    public <T> Optional<SparkDataAdapter<T>> getRatabaseAdapter(Class<T> type) {
        for (SparkDataAdapter adapter : registeredAdapters) {
            if (adapter.getType() == type) return Optional.of(adapter);
        }
        return Optional.empty();
    }
}
