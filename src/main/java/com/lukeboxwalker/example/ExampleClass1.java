package com.lukeboxwalker.example;

import com.lukeboxwalker.processing.annotation.Autowired;
import com.lukeboxwalker.processing.annotation.Service;

@Service
public class ExampleClass1 {

    @Autowired
    public ExampleClass1(final ExampleClass2 exampleClass2) {
        exampleClass2.foo();
        System.out.println("Created ExampleClass1");
    }
}
