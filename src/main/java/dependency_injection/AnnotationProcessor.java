package dependency_injection;


import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.lang.annotation.Annotation;
import java.util.Map;

public final class AnnotationProcessor extends ClassVisitor {

    private final Map<Class<? extends Annotation>,AnnotationHandler> handlerMap;

    public String className;

    public AnnotationProcessor(final Map<Class<? extends Annotation>, AnnotationHandler> handlerMap) {
        super(Opcodes.ASM5);
        this.handlerMap = handlerMap;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name.replace('/', '.');
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        Class<?> annotatedClass = null;
        try {
            for (Class<? extends Annotation> key : handlerMap.keySet()) {
                if (desc.equals(getFieldDescriptor(key))) {
                    if (annotatedClass == null) {
                        annotatedClass = Class.forName(className);
                    }
                    handlerMap.get(key).handle(annotatedClass);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String getFieldDescriptor(final Class<?> descClass) {
        return "L" + descClass.getName().replace(".", "/") + ";";
    }
}
