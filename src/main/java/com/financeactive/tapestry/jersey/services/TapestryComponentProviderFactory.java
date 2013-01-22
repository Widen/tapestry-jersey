package com.financeactive.tapestry.jersey.services;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProvider;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.core.spi.component.ioc.IoCInstantiatedComponentProvider;
import com.sun.jersey.core.spi.component.ioc.IoCManagedComponentProvider;
import org.apache.tapestry5.ioc.AnnotationProvider;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.internal.NullAnnotationProvider;

public class TapestryComponentProviderFactory implements IoCComponentProviderFactory {

    private final AnnotationProvider annotationProvider;

    private final ObjectLocator objectLocator;

    public TapestryComponentProviderFactory(ObjectLocator objectLocator) {
        this.annotationProvider = new NullAnnotationProvider();
        this.objectLocator = objectLocator;
    }

    @Override
    public IoCComponentProvider getComponentProvider(Class<?> c) {
        if (isTapestryManaged(c)) {
            return new ObjectLocatorComponentProvider<Object>(objectLocator, annotationProvider, c);
        } else if (isTapestryBuildable(c)) {
            return new AutoBuilderComponentProvider<Object>(objectLocator, c);
        }
        return null;
    }

    @Override
    public IoCComponentProvider getComponentProvider(ComponentContext cc, Class<?> c) {
        return getComponentProvider(c);
    }

    boolean isTapestryManaged(Class<?> c) {
        try {
            return objectLocator.getObject(c, annotationProvider) != null;
        } catch (RuntimeException e) {
            return false;
        }
    }

    boolean isTapestryBuildable(Class<?> c) {
        return false;
    }

    static class AutoBuilderComponentProvider<T> implements IoCInstantiatedComponentProvider {

        private final ObjectLocator objectLocator;

        private final Class<? extends T> type;

        AutoBuilderComponentProvider(ObjectLocator objectLocator, Class<? extends T> type) {
            this.objectLocator = objectLocator;
            this.type = type;
        }

        @Override
        public Object getInjectableInstance(Object o) {
            // TODO Dumb implementation
            return o;
        }

        @Override
        public Object getInstance() {
            return objectLocator.autobuild(type);
        }
    }

    static class ObjectLocatorComponentProvider<T> implements IoCManagedComponentProvider {

        private final ObjectLocator objectLocator;

        private final AnnotationProvider annotationProvider;

        private final Class<? extends T> type;

        ObjectLocatorComponentProvider(ObjectLocator objectLocator, AnnotationProvider annotationProvider, Class<? extends T> type) {
            this.objectLocator = objectLocator;
            this.annotationProvider = annotationProvider;
            this.type = type;
        }

        @Override
        public Object getInjectableInstance(Object o) {
            // TODO Dumb implementation
            return o;
        }

        @Override
        public Object getInstance() {
            return objectLocator.getObject(type, annotationProvider);
        }

        @Override
        public ComponentScope getScope() {
            return ComponentScope.Undefined;
        }
    }
}
