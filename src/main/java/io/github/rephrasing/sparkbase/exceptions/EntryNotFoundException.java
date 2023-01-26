package io.github.rephrasing.sparkbase.exceptions;

import lombok.Getter;

@Getter
public class EntryNotFoundException extends RuntimeException {

    private final String key;
    private final Class<?> valueClass;

    public EntryNotFoundException(String key, Class<?> valueClass, String customMessage) {
        super("Key: " + key + ", Value class: " + valueClass.getName() + "\nMESSAGE: " + customMessage);
        this.key = key;
        this.valueClass = valueClass;
    }

    public EntryNotFoundException(String key, Class<?> valueClass) {
        super("Key: " + key + ", Value class: " + valueClass.getName());
        this.key = key;
        this.valueClass = valueClass;
    }

}
