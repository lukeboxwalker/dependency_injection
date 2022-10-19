package com.lukeboxwalker.scheduler;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class TaskRunner implements Runnable {

    private final Runnable runnable;
    private final Consumer<BukkitRunnable> runner;

    public TaskRunner(final Runnable runnable, final Consumer<BukkitRunnable> runner) {
        this.runnable = runnable;
        this.runner = runner;
    }

    @Override
    public void run() {
        runner.accept(new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        });
    }
}
