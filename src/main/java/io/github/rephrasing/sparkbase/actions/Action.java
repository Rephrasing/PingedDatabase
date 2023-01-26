package io.github.rephrasing.sparkbase.actions;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class Action<T> {

    private final Supplier<T> supplier;

    public Action(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T execute() {
        return supplier.get();
    }

    @SneakyThrows
    public T executeAfter(long interval, TimeUnit timeUnit) {
        synchronized (supplier) {
            supplier.wait(timeUnit.toMillis(interval));
            return supplier.get();
        }
    }
}
