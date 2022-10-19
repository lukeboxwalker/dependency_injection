package com.lukeboxwalker.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class CommandAdapter extends org.bukkit.command.Command implements Command {

    public CommandAdapter(@NotNull String name) {
        super(name);
    }

    @Override
    public final boolean execute(@NotNull CommandSender sender, @NotNull String name, @NotNull String... args) {
        return execute(sender, args);
    }

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String... args);

    @NotNull
    @Override
    public final List<String> tabComplete(@NotNull CommandSender sender, @NotNull String name , @NotNull String... args) {
        return tabComplete(sender, args);
    }

    @NotNull
    public abstract List<String> tabComplete(@NotNull CommandSender sender, @NotNull String... args);
}
