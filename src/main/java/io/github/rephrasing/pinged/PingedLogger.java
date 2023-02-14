package io.github.rephrasing.pinged;

import io.github.rephrasing.pinged.logger.PrefixedLogger;
public class PingedLogger extends PrefixedLogger {
    PingedLogger() {
        super("Sparkbase");
    }

    public void formatInfo(String message, Object... objects) {
        info(String.format(message, objects));
    }
}
