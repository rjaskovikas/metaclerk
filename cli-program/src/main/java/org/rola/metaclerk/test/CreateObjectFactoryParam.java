package org.rola.metaclerk.test;


public interface CreateObjectFactoryParam<T,P> {
    @SuppressWarnings("EmptyMethod")
    T build(@SuppressWarnings("UnusedParameters") P param);
}
