package io.github.rephrasing.sparkbase.actions;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

public class VoidAction {

    private final Runnable runnable;

    public VoidAction(Runnable runnable) {
        this.runnable = runnable;
    }

    public void execute() {
        runnable.run();
    }

    @SneakyThrows
    public void executeAfter(long interval, TimeUnit timeUnit) {
        synchronized (runnable) {
            runnable.wait(timeUnit.toMillis(interval));
            runnable.run();
        }
    }

}
