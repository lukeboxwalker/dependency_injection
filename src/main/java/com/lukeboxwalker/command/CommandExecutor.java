package com.lukeboxwalker.command;

import org.bukkit.command.CommandSender;

public interface CommandExecutor {
    boolean execute(final CommandSender sender, Command command, final String... args);
}