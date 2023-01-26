package io.github.rephrasing.sparkbase;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class SparkLogger extends Logger {


    protected SparkLogger() {
        super("SparkBase", null);
        setParent(Sparkbase.rawLogger);
        setLevel(Level.ALL);
    }

    @Override
    public void log(LogRecord record) {
        record.setMessage("[Spark] " + record.getMessage());
        super.log(record);
    }
}
