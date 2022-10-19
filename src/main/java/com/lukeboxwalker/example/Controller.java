package com.lukeboxwalker.example;

import com.lukeboxwalker.processing.AnnotationController;

public class Controller {

    final AnnotationController annotationController = new AnnotationController();

    public void run() {
        annotationController.process(this);
    }
}
