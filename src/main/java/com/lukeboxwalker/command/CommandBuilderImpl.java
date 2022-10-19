package com.lukeboxwalker.command;

import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;

final class CommandBuilderImpl implements CommandBuilder, CommandExecutionBuilder {

    private final Plugin plugin;

    private String description;
    private String usage;
    private String permission;
    private String name;
    private String[] aliases;
    private CommandExecutor executor;
    private CommandTabCompleter tabCompleter;

    CommandBuilderImpl(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandExecutionBuilder description(final String description) {
        this.description = description == null ? "No description set." : description;
        return this;
    }

    @Override
    public CommandExecutionBuilder usage(final String usage) {
        this.usage = usage == null ? "No usage set." : usage;
        return this;
    }

    @Override
    public CommandExecutionBuilder permission(final String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public CommandExecutionBuilder name(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public CommandExecutionBuilder aliases(final String... aliases) {
        this.aliases = aliases == null ? new String[]{} : aliases;
        return this;
    }

    @Override
    public CommandExecutionBuilder executor(final CommandExecutor executor) {
        this.executor = executor == null ? (sender, command, args) -> false : executor;
        return this;
    }

    @Override
    public CommandExecutionBuilder tabCompleter(final CommandTabCompleter tabCompleter) {
        this.tabCompleter = tabCompleter == null ? (sender, command, args) -> new ArrayList<>() : tabCompleter;
        return this;
    }

    @Override
    public Command build() {
        if (name == null || name.isEmpty()) {
            throw new CommandLoadingException("Command name cannot be null or empty!");
        }
        final CommandImpl commandImpl = new CommandImpl(name);
        commandImpl.setAliases(Arrays.asList(aliases));
        commandImpl.setDescription(description);
        commandImpl.setUsage(usage);
        commandImpl.setPermission(permission);
        commandImpl.setExecutor(executor);
        commandImpl.setTabCompleter(tabCompleter);
        return commandImpl;
    }

    @Override
    public Command buildAndRegister() {
        return CommandUtils.registerCommand(build(), plugin);
    }

    @Override
    public void register() {
        this.buildAndRegister();
    }
}
