package com.lukeboxwalker.processing;

import com.lukeboxwalker.processing.annotation.AutowiredConstructor;
import com.lukeboxwalker.processing.annotation.PluginComponent;
import com.lukeboxwalker.processing.exception.ObjectCreationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComponentFactoryTest {

    private ComponentFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ComponentFactoryImpl();
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

    @PluginComponent
    private static class Component {

    }

    @PluginComponent
    private static class InterfaceComponent implements ExampleInterface {
    }

    private interface ExampleInterface {

    }

    @PluginComponent
    private static class NestedComponent {

        private final Component component;

        @AutowiredConstructor
        public NestedComponent(final Component component) {
            this.component = component;
        }

        public Component getComponent() {
            return component;
        }
    }

    @PluginComponent
    private static class NoDefaultConstructor {

        private final Object object;

        public NoDefaultConstructor(final Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }
    }

    @PluginComponent
    private static class CycleComponentOne {

        @AutowiredConstructor
        public CycleComponentOne(final CycleComponentTwo cycleComponentTwo) {
        }
    }

    @PluginComponent
    private static class CycleComponentTwo {

        @AutowiredConstructor
        public CycleComponentTwo(final CycleComponentOne cycleComponentOne) {
        }
    }

    @PluginComponent
    private static class SelfDependency {

        @AutowiredConstructor
        public SelfDependency(final SelfDependency selfDependency) {
        }
    }
}