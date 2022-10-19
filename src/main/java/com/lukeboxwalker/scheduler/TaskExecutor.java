package com.lukeboxwalker.scheduler;

public interface TaskExecutor {

    Runnable execute(final Runnable runnable);
}
