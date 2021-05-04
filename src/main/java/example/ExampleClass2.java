package example;

import dependency_injection.Autowired;
import dependency_injection.Service;

@Service
public class ExampleClass2 {

    @Autowired
    public ExampleClass2() {
        System.out.println("Created ExampleClass2");
    }
}
