package com.lukeboxwalker.scheduler;

import org.bukkit.plugin.Plugin;

public final class SchedulerUtils {

    private SchedulerUtils() {
        super();
    }

    public static TaskExecutor sync(final Plugin plugin) {
        return runnable -> new TaskRunner(runnable, bukkitRunnable ->
                bukkitRunnable.runTask(plugin));
    }

    public static TaskExecutor async(final Plugin plugin) {
        return runnable -> new TaskRunner(runnable, bukkitRunnable ->
                bukkitRunnable.runTaskAsynchronously(plugin));
    }

    public static TaskExecutor taskLater(final Plugin plugin, final int delay) {
        return runnable -> new TaskRunner(runnable, bukkitRunnable ->
                bukkitRunnable.runTaskLater(plugin, delay));
    }

    public static TaskExecutor asyncTaskLater(final Plugin plugin, final int delay) {
        return runnable -> new TaskRunner(runnable, bukkitRunnable ->
                bukkitRunnable.runTaskLaterAsynchronously(plugin, delay));
    }

    public static TaskExecutor syncTimerTask(final Plugin plugin, final int delay, final int period) {
        return runnable -> new TaskRunner(runnable, bukkitRunnable ->
                bukkitRunnable.runTaskTimer(plugin, delay, period));
    }

    public static TaskExecutor asyncTimerTask(final Plugin plugin, final int delay, final int period) {
        return runnable -> new TaskRunner(runnable, bukkitRunnable ->
                bukkitRunnable.runTaskTimerAsynchronously(plugin, delay, period));
    }
}
