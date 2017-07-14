package com.transsion.json;

import com.transsion.json.factories.ExistingObjectFactory;
import java.util.Map;
import java.util.HashMap;

public class JSONDeserializer<T> {

    private final Map<Class, ObjectFactory> typeFactories = new HashMap<>();
    private final Map<Path, ObjectFactory> pathFactories = new HashMap<>();

    public JSONDeserializer() {
    }

    public T deserialize(String input) {
        ObjectBinder binder = createObjectBinder();
        return (T) binder.bind(new JSONTokener(input).nextValue());
    }

    public T deserialize(String input, Class root) {
        ObjectBinder binder = createObjectBinder();
        return (T) binder.bind(new JSONTokener(input).nextValue(), root);
    }

    public T deserialize(String input, ObjectFactory factory) {
        use((String) null, factory);
        ObjectBinder binder = createObjectBinder();
        return (T) binder.bind(new JSONTokener(input).nextValue());
    }


    public T deserializeInto(String input, T target) {
        return deserialize(input, new ExistingObjectFactory(target));
    }

    public JSONDeserializer<T> use(Class clazz, ObjectFactory factory) {
        typeFactories.put(clazz, factory);
        if (clazz == Boolean.class) typeFactories.put(Boolean.TYPE, factory);
        else if (clazz == Integer.class) typeFactories.put(Integer.TYPE, factory);
        else if (clazz == Short.class) typeFactories.put(Short.TYPE, factory);
        else if (clazz == Long.class) typeFactories.put(Long.TYPE, factory);
        else if (clazz == Byte.class) typeFactories.put(Byte.TYPE, factory);
        else if (clazz == Float.class) typeFactories.put(Float.TYPE, factory);
        else if (clazz == Double.class) typeFactories.put(Double.TYPE, factory);
        else if (clazz == Character.class) typeFactories.put(Character.TYPE, factory);
        return this;
    }

    public JSONDeserializer<T> use(String path, ObjectFactory factory) {
        pathFactories.put(Path.parse(path), factory);
        return this;
    }

    private ObjectBinder createObjectBinder() {
        ObjectBinder binder = new ObjectBinder();
        for (Class clazz : typeFactories.keySet()) {
            binder.use(clazz, typeFactories.get(clazz));
        }
        for (Path p : pathFactories.keySet()) {
            binder.use(p, pathFactories.get(p));
        }
        return binder;
    }

}
