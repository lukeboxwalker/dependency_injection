package com.lukeboxwalker.command;

import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Managing Command registration.
 *
 * @author Walkehorst Lukas
 * @since 1.0
 */
public final class CommandUtils {

    private static Constructor<PluginCommand> COMMAND_CONSTRUCTOR = null;

    static {
        try {
            COMMAND_CONSTRUCTOR = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            COMMAND_CONSTRUCTOR.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private CommandUtils() {
        super();
    }

    public static Command registerCommand(final Command command, final Plugin plugin) {
        if (COMMAND_CONSTRUCTOR != null) {
            try {
                final PluginCommand pluginCommand = COMMAND_CONSTRUCTOR.newInstance(command.getName(), plugin);
                pluginCommand.setPermission(command.getPermission());
                pluginCommand.setAliases(command.getAliases());
                pluginCommand.setDescription(command.getDescription());
                pluginCommand.setUsage(command.getUsage());
                pluginCommand.setExecutor((sender, cmd, name, args) -> command.execute(sender, name, args));
                pluginCommand.setTabCompleter((sender, cmd, name, args) -> command.tabComplete(sender, name, args));
                plugin.getServer().getCommandMap().register(pluginCommand.getName(), pluginCommand);
                return pluginCommand;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException cause) {
                throw new CommandLoadingException(cause);
            }
        } else {
            throw new CommandLoadingException("PluginCommand constructor not found!");
        }
    }

    public static CommandBuilder commandBuilder(final Plugin plugin) {
        return new CommandBuilderImpl(plugin);
    }
}
