package com.lukeboxwalker.example;

import com.lukeboxwalker.processing.annotation.Autowired;
import com.lukeboxwalker.processing.annotation.Service;

@Service
public class ExampleClass2 {

    @Autowired
    public ExampleClass2() {
        System.out.println("Created ExampleClass2");
    }

    public void foo() {

    }
}
