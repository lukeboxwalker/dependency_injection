package example;

import dependency_injection.AnnotationController;

public final class Controller {

    public static void main(final String... args) {
        final AnnotationController annotationController = new AnnotationController();
        annotationController.process(new Controller());
    }
}
