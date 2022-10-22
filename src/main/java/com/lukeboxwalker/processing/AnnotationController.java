package com.lukeboxwalker.processing;

import com.lukeboxwalker.plugin.PluginApi;
import com.lukeboxwalker.processing.annotation.AutoRegisterCommand;
import com.lukeboxwalker.processing.annotation.AutoRegisterListener;
import com.lukeboxwalker.processing.annotation.PluginComponent;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class AnnotationController {

    private static final int PARSING_OPTIONS = 0;
    private static final String CLASS_EXTENSION = ".class";

    private final ComponentFactoryImpl objectFactory = new ComponentFactoryImpl();
    private final Map<Class<? extends Annotation>, ComponentAnnotationHandler> handlerMap = new HashMap<>();

    public AnnotationController() {
        super();
    }

    public void process(final PluginApi pluginApi) {
        handlerMap.put(PluginComponent.class, new ServiceAnnotationHandler(objectFactory));
        handlerMap.put(AutoRegisterListener.class, new AutoRegisterListenerHandler(objectFactory, pluginApi));
        handlerMap.put(AutoRegisterCommand.class, new AutoRegisterCommandHandler(objectFactory, pluginApi));
        try {
            this.objectFactory.provide(pluginApi);
            this.scanClassPath(pluginApi);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ComponentFactory getComponentFactory() {
        return objectFactory;
    }

    private List<String> listAllClasses(final File dir, List<String> result) {
        assert dir.isDirectory();
        final File[] files = Optional.ofNullable(dir.listFiles()).orElse(new File[0]);
        for (final File file : files) {
            if (file.isDirectory()) {
                listAllClasses(file, result);
            }
            if (file.isFile()) {
                if (isClass(file.getName())) {
                    result.add(file.getAbsolutePath());
                }
            }
        }
        return result;
    }

    private boolean isClass(final String name) {
        return name.endsWith(CLASS_EXTENSION);
    }

    private void scanBuildPath(final URL url, final ClassVisitor visitor) throws IOException {
        final File dir = new File(url.getPath());
        for (String path : listAllClasses(dir, new ArrayList<>())) {
            try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
                new ClassReader(inputStream).accept(visitor, PARSING_OPTIONS);
            }
        }
    }

    private void scanJarFile(final URL url, final ClassVisitor visitor) throws IOException {
        final JarFile jarFile = new JarFile(url.getPath());
        for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
            JarEntry entry = entries.nextElement();
            if (isClass(entry.getName())) {
                try (InputStream inputStream = jarFile.getInputStream(entry)) {
                    new ClassReader(inputStream).accept(visitor, PARSING_OPTIONS);
                }
            }
        }
    }

    private void scanClassPath(final Object root) throws IOException {
        final URL url = root.getClass().getProtectionDomain().getCodeSource().getLocation();
        final AnnotationProcessor processor = new AnnotationProcessor(handlerMap);
        try {
            scanJarFile(url, processor);
        } catch (Exception e) {
            e.printStackTrace();
            scanBuildPath(url, processor);
        }
    }
}
