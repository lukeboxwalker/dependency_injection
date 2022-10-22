package com.lukeboxwalker.processing;

import com.lukeboxwalker.plugin.PluginApi;
import org.bukkit.event.Listener;

import java.util.Arrays;

public class AutoRegisterListenerHandler implements ComponentAnnotationHandler {

    private final ComponentFactory componentFactory;
    private final PluginApi pluginApi;

    public AutoRegisterListenerHandler(final ComponentFactory componentFactory, final PluginApi pluginApi) {
        this.componentFactory = componentFactory;
        this.pluginApi = pluginApi;
    }

    @Override
    public void handle(final Class<?> annotatedClass) {
        if (Arrays.asList(annotatedClass.getInterfaces()).contains(Listener.class)) {
            final Listener listener = (Listener) componentFactory.get(annotatedClass);
            pluginApi.registerListener(listener);
//            System.out.println("Register Listener '" + annotatedClass.getName()
//                    + "' by Plugin '" + pluginApi.getName() + "'");
        } else {
//            System.out.println("Could not register Listener " + annotatedClass.getName()
//                    + ". Interface 'Listener.class' not implemented!");
        }
    }
}
