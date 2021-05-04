package dependency_injection;

import jdk.internal.org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class AnnotationController {

    private final ObjectFactoryImpl componentFactory = new ObjectFactoryImpl();
    private final Map<Class<? extends Annotation>, AnnotationHandler> handlerMap = new HashMap<>();

    public AnnotationController() {
        super();
        handlerMap.put(Service.class, new ServiceAnnotationHandler(componentFactory));
    }

    public void process(final Object root) {
        this.componentFactory.provide(root);
        try {
            this.scanClassPath(root.getClass().getClassLoader());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObjectFactory getComponentFactory() {
        return componentFactory;
    }

    private void visitFile(final File file) throws IOException {
        if (file.isDirectory()) {
            final File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    visitFile(child);
                }
            }
        } else if (file.getName().endsWith(".class")) {
            try (FileInputStream inputStream = new FileInputStream(file)) {
                new ClassReader(inputStream).accept(new AnnotationProcessor(handlerMap), 0);
            }
        }
    }

    private void scanClassPath(final ClassLoader pluginClassLoader) throws IOException {
        if (pluginClassLoader instanceof URLClassLoader) {
            final URL[] urls = ((URLClassLoader) pluginClassLoader).getURLs();
            if (urls.length == 1) {
                final JarFile jarFile = new JarFile(urls[0].getPath());
                AnnotationProcessor annotationProcessor;
                for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        annotationProcessor = new AnnotationProcessor(handlerMap);
                        new ClassReader(jarFile.getInputStream(entry)).accept(annotationProcessor, 0);
                    }
                }
            } else {
                for (URL url : urls) {
                    if (url.toString().contains("/target/classes/")) {
                        try {
                            final File file = new File(url.toURI());
                            visitFile(file);
                        } catch (URISyntaxException e) {
                            throw new IllegalStateException("Could not load class!", e);
                        }
                        return;
                    }
                }
            }
        } else {
            throw new IllegalStateException("Could not load class!");
        }
    }
}
