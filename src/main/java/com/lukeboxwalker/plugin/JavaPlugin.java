package com.lukeboxwalker.plugin;

import com.lukeboxwalker.command.CommandBuilder;
import com.lukeboxwalker.command.CommandUtils;
import com.lukeboxwalker.processing.AnnotationController;
import com.lukeboxwalker.processing.ComponentFactory;
import com.lukeboxwalker.processing.annotation.AutowiredConstructor;
import com.lukeboxwalker.processing.annotation.PluginComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

@PluginComponent
public abstract class JavaPlugin extends org.bukkit.plugin.java.JavaPlugin implements PluginApi {

    private final AnnotationController annotationController = new AnnotationController();

    @AutowiredConstructor
    public JavaPlugin() {
        super();
    }

    @AutowiredConstructor
    public JavaPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
       super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.annotationController.process(this);
    }

    @Override
    public Command registerCommand(final Command command) {
        return CommandUtils.registerCommand(command, this);
    }

    @Override
    public CommandBuilder commandBuilder() {
        return CommandUtils.commandBuilder(this);
    }

    @Override
    public ComponentFactory getComponentFactory() {
        return annotationController.getComponentFactory();
    }

    @Override
    public void registerListener(final Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }
}
