package com.lukeboxwalker.command;

import java.util.Set;

public interface BaseCommand {

    void setSubCommands(Set<Command> subCommands);

    Set<Command> getSubCommands();

    void addSubCommand(final Command subCommand);
}
