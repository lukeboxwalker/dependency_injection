package example;

import dependency_injection.Autowired;
import dependency_injection.Service;

@Service
public class ExampleClass1 {

    @Autowired
    public ExampleClass1(final ExampleClass2 exampleClass2) {
        exampleClass2.foo();
        System.out.println("Created ExampleClass1");
    }
}
