package com.lukeboxwalker.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginBaseCommand extends CommandAdapter implements BaseCommand {

    private Set<Command> subCommands = new HashSet<>();

    public PluginBaseCommand(final String name) {
        super(name);
    }

    public void setSubCommands(Set<Command> subCommands) {
        this.subCommands = subCommands;
    }

    public Set<Command> getSubCommands() {
        return subCommands;
    }

    public void addSubCommand(final Command subCommand) {
        subCommands.add(subCommand);
    }

    @Override
    public boolean execute(final @Nonnull CommandSender sender, final @Nonnull String[] args) {
        if (args.length == 0) {
            return true;
        } else {
            final String[] subArgument = getSubArguments(args);
            final Optional<Command> optional = subCommands.stream()
                    .filter(command -> command.getName().equals(args[0]))
                    .findAny();
            return optional.map(command -> command.execute(sender, subArgument)).orElse(false);
        }
    }

    @NotNull
    @Override
    public List<String> tabComplete(final @Nonnull CommandSender sender, final @Nonnull String[] args) {
        if (args.length == 1) {
            return subCommands.stream()
                    .map(Command::getName)
                    .filter(commandName -> commandName.startsWith(args[0]))
                    .collect(Collectors.toList());
        } else {
            final String[] subArgument = getSubArguments(args);
            final Optional<Command> optional = subCommands.stream()
                    .filter(command -> command.getName().equals(args[0]))
                    .findAny();
            return optional.map(command -> command.tabComplete(sender, subArgument)).orElse(new ArrayList<>());
        }
    }

    private String[] getSubArguments(final String... args) {
        return Arrays.copyOfRange(args, 1, args.length);
    }
}
