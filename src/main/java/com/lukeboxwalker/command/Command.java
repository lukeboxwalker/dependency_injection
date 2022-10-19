package com.lukeboxwalker.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Command {

    String getUsage();

    String getDescription();

    String getPermissionMessage();

    List<String> getAliases();

    String getLabel();

    boolean testPermissionSilent(@NotNull CommandSender target);

    boolean testPermission(@NotNull CommandSender target);

    String getPermission();

    String getTimingName();

    String getName();

    boolean execute(@NotNull CommandSender sender, @NotNull String... args);

    List<String> tabComplete(@NotNull CommandSender sender, @NotNull String... args);
}
