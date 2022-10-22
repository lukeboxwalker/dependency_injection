package com.lukeboxwalker.processing;

import com.lukeboxwalker.processing.exception.AnnotationProcessingError;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.annotation.Annotation;
import java.util.Map;

public final class AnnotationProcessor extends ClassVisitor {

    private final Map<Class<? extends Annotation>, ComponentAnnotationHandler> handlerMap;

    public String className;

    public AnnotationProcessor(final Map<Class<? extends Annotation>, ComponentAnnotationHandler> handlerMap) {
        super(Opcodes.ASM9);
        this.handlerMap = handlerMap;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name.replace('/', '.');
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        try {
            handlerMap.getOrDefault(loadClassFromDesc(desc), (annotatedClass) -> {}).handle(Class.forName(className));
            return null;
        } catch (ClassNotFoundException e) {
            throw new AnnotationProcessingError(e);
        }
    }

    @Override
    public void visitEnd() {
        className = null;
    }

    private Class<?> loadClassFromDesc(final String desc) throws ClassNotFoundException {
        final String name = desc.replaceAll("[L;]", "").replace('/', '.');
        System.out.println(name);
        System.out.println(Class.forName(name));
        return Class.forName(name);
    }
}
