package io.github.rephrasing.pinged.pinging;


import java.util.concurrent.TimeUnit;

public class PingedProcess {

    private final long startMills;

    public PingedProcess() {
        this.startMills = System.currentTimeMillis();
    }

    public long end() {
        return System.currentTimeMillis() - startMills;
    }

    public long end(TimeUnit unit) {
        return unit.convert(startMills, TimeUnit.MILLISECONDS);
    }
}
