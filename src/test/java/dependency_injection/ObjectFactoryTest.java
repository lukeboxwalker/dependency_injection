package dependency_injection;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectFactoryTest {

    private ObjectFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ObjectFactoryImpl();
    }

    @Test
    void testDependencyInversion() {
        factory.get(InterfaceComponent.class);
        final ExampleInterface exampleInterface = factory.get(ExampleInterface.class);
        Assertions.assertNotNull(exampleInterface);
    }

    @Test
    void testComponentInstantiation() {
        final Component mock = factory.get(Component.class);
        Assertions.assertNotNull(mock);
    }

    @Test
    void testNoDuplicateComponent() {
        final Component mock = factory.get(Component.class);
        Assertions.assertEquals(factory.get(Component.class), mock);
    }

    @Test
    void testNestedComponent() {
        final NestedComponent nestedMock = factory.get(NestedComponent.class);
        Assertions.assertNotNull(nestedMock);
        Assertions.assertNotNull(nestedMock.getComponent());
    }

    @Test
    void testNoDuplicateNestedDependency() {
        final NestedComponent nestedMock = factory.get(NestedComponent.class);
        final Component mock = factory.get(Component.class);
        Assertions.assertEquals(nestedMock.getComponent(), mock);
    }

    @Test
    void testNoDefaultConstructor() {
        Assertions.assertThrows(ObjectCreationException.class, () -> {
            final NoDefaultConstructor noDefaultConstructor = factory.get(NoDefaultConstructor.class);
            Assertions.assertNotNull(noDefaultConstructor);
            Assertions.assertNotNull(noDefaultConstructor.getObject());
        });
    }

    @Test
    void testNoDependencyCycle() {
        Assertions.assertThrows(ObjectCreationException.class, () -> {
            final CycleComponentOne cycleComponentOne = factory.get(CycleComponentOne.class);
            Assertions.assertNotNull(cycleComponentOne);
        });
    }

    @Test
    void testNoSelfCycle() {
        Assertions.assertThrows(ObjectCreationException.class, () -> {
            final SelfDependency selfDependency = factory.get(SelfDependency.class);
            Assertions.assertNotNull(selfDependency);
        });
    }

    @Service
    private static class Component {

    }

    @Service
    private static class InterfaceComponent implements ExampleInterface {
    }

    private interface ExampleInterface {

    }

    @Service
    private static class NestedComponent {

        private final Component component;

        @Autowired
        public NestedComponent(final Component component) {
            this.component = component;
        }

        public Component getComponent() {
            return component;
        }
    }

    @Service
    private static class NoDefaultConstructor {

        private final Object object;

        public NoDefaultConstructor(final Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }
    }

    @Service
    private static class CycleComponentOne {

        @Autowired
        public CycleComponentOne(final CycleComponentTwo cycleComponentTwo) {
        }
    }

    @Service
    private static class CycleComponentTwo {

        @Autowired
        public CycleComponentTwo(final CycleComponentOne cycleComponentOne) {
        }
    }

    @Service
    private static class SelfDependency {

        @Autowired
        public SelfDependency(final SelfDependency selfDependency) {
        }
    }
}