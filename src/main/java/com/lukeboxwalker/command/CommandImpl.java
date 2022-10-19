package com.lukeboxwalker.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class CommandImpl extends CommandAdapter {

    private CommandExecutor executor;
    private CommandTabCompleter tabCompleter;

    CommandImpl(@NotNull String name) {
        super(name);
    }

    public void setExecutor(CommandExecutor executor) {
        this.executor = executor;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String... args) {
        return executor.execute(sender, this, args);
    }

    public void setTabCompleter(CommandTabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String... args) {
        return tabCompleter.tabComplete(sender, this, args);
    }
}
