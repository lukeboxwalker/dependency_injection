package com.lukeboxwalker.plugin;

import com.lukeboxwalker.command.CommandBuilder;
import com.lukeboxwalker.processing.ComponentFactory;
import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public interface PluginApi extends Plugin {

    Command registerCommand(final Command command);

    CommandBuilder commandBuilder();

    ComponentFactory getComponentFactory();

    void registerListener(final Listener listener);
}
