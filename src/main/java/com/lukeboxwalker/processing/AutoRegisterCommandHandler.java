package com.lukeboxwalker.processing;

import com.lukeboxwalker.plugin.PluginApi;
import org.bukkit.command.Command;

public class AutoRegisterCommandHandler implements ComponentAnnotationHandler {


    private final ComponentFactory componentFactory;
    private final PluginApi pluginApi;

    public AutoRegisterCommandHandler(final ComponentFactory componentFactory, final PluginApi pluginApi) {
        this.componentFactory = componentFactory;
        this.pluginApi = pluginApi;
    }

    @Override
    public void handle(final Class<?> annotatedClass) {
        Class<?> superClass = annotatedClass;
        while (!superClass.equals(Object.class)) {
            superClass = superClass.getSuperclass();
            if (superClass.equals(Command.class)) {
                final Command command = (Command) componentFactory.get(annotatedClass);
                pluginApi.registerCommand(command);
                return;
            }
        }
    }
}
