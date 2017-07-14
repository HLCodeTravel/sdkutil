package com.transsion.json;

public class DeferredInstantiation<T> {

    private final Class<? extends T> clazz;
    private T instantiate;

    public DeferredInstantiation(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

    public synchronized T get() throws IllegalAccessException, InstantiationException {
        if( instantiate == null ) {
            instantiate = clazz.newInstance();
        }
        return instantiate;
    }
}
