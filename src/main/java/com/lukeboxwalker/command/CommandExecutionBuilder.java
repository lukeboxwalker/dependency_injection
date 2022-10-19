package com.lukeboxwalker.command;

import org.bukkit.command.Command;

public interface CommandExecutionBuilder {

    CommandExecutionBuilder description(final String description);

    CommandExecutionBuilder usage(final String usage);

    CommandExecutionBuilder permission(final String permission);

    CommandExecutionBuilder aliases(final String... aliases);

    CommandExecutionBuilder executor(final CommandExecutor executor);

    CommandExecutionBuilder tabCompleter(final CommandTabCompleter tabCompleter);

    Command buildAndRegister();

    Command build();

    void register();
}
