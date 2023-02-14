package io.github.rephrasing.pinged.logger;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class PrefixedLogger extends Logger {

    private final String prefix;

    public PrefixedLogger(String prefix) {
        super(prefix, null);
        this.prefix = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
        Logger raw = Logger.getLogger(prefix);
        setParent(raw);
        setLevel(Level.ALL);
    }

    @Override
    public void log(LogRecord record) {
        record.setMessage('[' + prefix + ']' + record.getMessage());
        super.log(record);
    }
}
