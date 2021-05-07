package example;

import dependency_injection.AnnotationController;

public class Controller {

    final AnnotationController annotationController = new AnnotationController();

    public void run() {
        annotationController.process(this);
    }
}
