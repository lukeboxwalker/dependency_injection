package com.lukeboxwalker.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface CommandTabCompleter {
    List<String> tabComplete(final CommandSender sender, Command command, final String... args);
}