package com.transsion.json;

import com.transsion.json.factories.ArrayObjectFactory;
import com.transsion.json.factories.BeanObjectFactory;
import com.transsion.json.factories.BooleanObjectFactory;
import com.transsion.json.factories.ByteObjectFactory;
import com.transsion.json.factories.CharacterObjectFactory;
import com.transsion.json.factories.ClassLocatorObjectFactory;
import com.transsion.json.factories.DoubleObjectFactory;
import com.transsion.json.factories.EnumObjectFactory;
import com.transsion.json.factories.FloatObjectFactory;
import com.transsion.json.factories.IntegerObjectFactory;
import com.transsion.json.factories.JsonNumberObjectFactory;
import com.transsion.json.factories.ListObjectFactory;
import com.transsion.json.factories.LongObjectFactory;
import com.transsion.json.factories.MapObjectFactory;
import com.transsion.json.factories.SetObjectFactory;
import com.transsion.json.factories.StringObjectFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectBinder {

    private final LinkedList<Object> objectStack = new LinkedList<>();
    private final LinkedList<Object> jsonStack = new LinkedList<>();
    private final Path currentPath = new Path();
    private final Map<Class, ObjectFactory> factories;
    private final Map<Path, ObjectFactory> pathFactories = new HashMap<>();

    public ObjectBinder() {
        factories = new HashMap<>();
        factories.put(Object.class, new BeanObjectFactory());
        factories.put(Collection.class, new ListObjectFactory());
        factories.put(List.class, new ListObjectFactory());
        factories.put(Set.class, new SetObjectFactory());
        factories.put(Map.class, new MapObjectFactory());
        factories.put(Integer.class, new IntegerObjectFactory());
        factories.put(int.class, new IntegerObjectFactory());
        factories.put(Float.class, new FloatObjectFactory());
        factories.put(float.class, new FloatObjectFactory());
        factories.put(Double.class, new DoubleObjectFactory());
        factories.put(double.class, new DoubleObjectFactory());
        factories.put(Long.class, new LongObjectFactory());
        factories.put(long.class, new LongObjectFactory());
        factories.put(Byte.class, new ByteObjectFactory());
        factories.put(byte.class, new ByteObjectFactory());
        factories.put(Boolean.class, new BooleanObjectFactory());
        factories.put(boolean.class, new BooleanObjectFactory());
        factories.put(Character.class, new CharacterObjectFactory());
        factories.put(char.class, new CharacterObjectFactory());
        factories.put(Enum.class, new EnumObjectFactory());
        factories.put(String.class, new StringObjectFactory());
        factories.put(Array.class, new ArrayObjectFactory());
        factories.put(JsonNumber.class, new JsonNumberObjectFactory());
    }

    public ObjectBinder use(Path path, ObjectFactory factory) {
        pathFactories.put(path, factory);
        return this;
    }

    public ObjectBinder use(Class clazz, ObjectFactory factory) {
        factories.put(clazz, factory);
        return this;
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public Object bind(Object input) {
        return this.bind(input, null);
    }

    public Object bind(Object source, Object target) {
        if (target instanceof Map) {
            bindIntoMap((Map) source, (Map<Object, Object>) target, null, null);
        } else if (target instanceof Collection) {
            bindIntoCollection((Collection) source, (Collection<Object>) target, null);
        } else {
            bindIntoObject((Map) source, target, target.getClass());
        }
        return target;
    }

    public Object bind(Object input, Type targetType) {
        jsonStack.add(input);
        try {
            if (input == null) return null;
            Class targetClass = findClassName(input, getTargetClass(targetType));
            ObjectFactory factory = findFactoryFor(targetClass);
            if (factory == null)
                throw new JSONException(currentPath + ": + Could not find a suitable ObjectFactory for " + targetClass);
            return factory.instantiate(this, input, targetType, targetClass);
        } finally {
            jsonStack.removeLast();
        }
    }

    public <T extends Collection<Object>> T bindIntoCollection(Collection value, T target, Type targetType) {
        Type valueType = null;
        if (targetType instanceof ParameterizedType) {
            valueType = ((ParameterizedType) targetType).getActualTypeArguments()[0];
        }
        jsonStack.add(value);
        objectStack.add(target);
        getCurrentPath().enqueue("values");
        for (Object obj : value) {
            target.add(bind(obj, valueType));
        }
        getCurrentPath().pop();
        objectStack.removeLast();
        jsonStack.removeLast();
        return target;
    }

    public Object bindIntoMap(Map input, Map<Object, Object> result, Type keyType, Type valueType) {
        jsonStack.add(input);
        objectStack.add(result);
        for (Object inputKey : input.keySet()) {
            currentPath.enqueue("keys");
            Object key = bind(inputKey, keyType);
            currentPath.pop();
            currentPath.enqueue("values");
            Object value = bind(input.get(inputKey), valueType);
            currentPath.pop();
            result.put(key, value);
        }
        objectStack.removeLast();
        jsonStack.removeLast();
        return result;
    }

    public Object bindIntoObject(Map jsonOwner, Object target, Type targetType) {
        try {
            objectStack.add(target);
            BeanAnalyzer analyzer = BeanAnalyzer.analyze(target.getClass());
            for (BeanProperty descriptor : analyzer != null ? analyzer.getProperties() : null) {
                if (containsFieldInJson(jsonOwner, descriptor)) {
                    Object value = findFieldInJson(jsonOwner, descriptor);
                    if (descriptor.isWritable()) {
                        currentPath.enqueue(descriptor.getName());
                        Method setMethod = descriptor.getWriteMethod();
                        if (setMethod != null) {
                            Type[] types = setMethod.getGenericParameterTypes();
                            if (types.length == 1) {
                                Type paramType = types[0];
                                setMethod.invoke(objectStack.getLast(), bind(value, resolveParameterizedTypes(paramType, targetType)));
                            } else {
                                throw new JSONException(currentPath + ":  Expected a single parameter for method " + target.getClass().getName() + "." + setMethod.getName() + " but got " + types.length);
                            }
                        } else {
                            Field field = descriptor.getProperty();
                            if (field != null) {
                                field.setAccessible(true);
                                field.set(target, bind(value, field.getGenericType()));
                            }
                        }
                        currentPath.pop();
                    }
                }
            }
            return objectStack.removeLast();
        } catch (IllegalAccessException e) {
            throw new JSONException(currentPath + ":  Could not access the no-arg constructor for " + target.getClass().getName(), e);
        } catch (InvocationTargetException ex) {
            throw new JSONException(currentPath + ":  Exception while trying to invoke setter method.", ex);
        }
    }

    public JSONException cannotConvertValueToTargetType(Object value, Class targetType) {
        return new JSONException(String.format("%s:  Can not convert %s into %s", currentPath, value.getClass().getName(), targetType.getName()));
    }

    private Class getTargetClass(Type targetType) {
        if (targetType == null) {
            return null;
        } else if (targetType instanceof Class) {
            return (Class) targetType;
        } else if (targetType instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) targetType).getRawType();
        } else if (targetType instanceof GenericArrayType) {
            return Array.class;
        } else if (targetType instanceof WildcardType) {
            return null; // nothing you can do about these.  User will have to specify this with use()
        } else if (targetType instanceof TypeVariable) {
            return null; // nothing you can do about these.  User will have to specify this with use()
        } else {
            throw new JSONException(currentPath + ":  Unknown type " + targetType);
        }
    }

    private Type resolveParameterizedTypes(Type genericType, Type targetType) {
        if (genericType instanceof Class) {
            return genericType;
        } else if (genericType instanceof ParameterizedType) {
            return genericType;
        } else if (genericType instanceof TypeVariable) {
            return targetType;
        } else if (genericType instanceof WildcardType) {
            return targetType;
        } else if (genericType instanceof GenericArrayType) {
            return ((GenericArrayType) genericType).getGenericComponentType();
        } else {
            throw new JSONException(currentPath + ":  Unknown generic type " + genericType + ".");
        }
    }


    private Class findClassName(Object map, Class targetType) throws JSONException {
        if (!pathFactories.containsKey(currentPath)) {
            Class mostSpecificType = useMostSpecific(map instanceof Map ? findClassInMap((Map) map, null) : null, targetType);
            if (mostSpecificType == null) {
                return map.getClass();
            } else {
                return mostSpecificType;
            }
        } else {
            return null;
        }
    }

    protected Class useMostSpecific(Class classFromTarget, Class typeFound) {
        if (classFromTarget != null && typeFound != null) {
            return typeFound.isAssignableFrom(classFromTarget) ? classFromTarget : typeFound;
        } else if (typeFound != null) {
            return typeFound;
        } else if (classFromTarget != null) {
            return classFromTarget;
        } else {
            return null;
        }
    }

    protected Class findClassInMap(Map map, Class override) {
        if (override == null) {
            String classname = (String) map.get("class");
            try {
                if (classname != null) {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    if (classLoader != null) {
                        return classLoader.loadClass(classname);
                    } else {
                        return Class.forName(classname);
                    }
                }
                return null;
            } catch (ClassNotFoundException e) {
                throw new JSONException(String.format("%s:  Could not load %s", currentPath, classname), e);
            }
        } else {
            return override;
        }
    }

    private ObjectFactory findFactoryFor(Class targetType) {
        ObjectFactory factory = pathFactories.get(currentPath);
        if (factory == null) {
            if (targetType != null && targetType.isArray()) return factories.get(Array.class);
            return findFactoryByTargetClass(targetType);
        }
        return factory;
    }

    private ObjectFactory findFactoryByTargetClass(Class targetType) {
        ObjectFactory factory;
        factory = factories.get(targetType);
        if (factory == null && targetType != null) {
            for (Class intf : targetType.getInterfaces()) {
                factory = findFactoryByTargetClass(intf);
                if (factory != null) return factory;
            }
            if (targetType.getSuperclass() != null) {
                return findFactoryByTargetClass(targetType.getSuperclass());
            }
            return null;
        } else {
            return factory;
        }
    }

    protected Object instantiate(Class clazz) {
        try {
            Constructor constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new JSONException(currentPath + ":There was an exception trying to instantiate an instance of " + clazz.getName(), e);
        } catch (InvocationTargetException e) {
            throw new JSONException(currentPath + ":There was an exception trying to instantiate an instance of " + clazz.getName(), e);
        }
    }

    private boolean containsFieldInJson(Map map, BeanProperty property) {
        return map.containsKey(property.getJsonName()) || map.containsKey(upperCase(property.getJsonName()));
    }

    private Object findFieldInJson(Map map, BeanProperty property) {
        Object value = map.get(property.getJsonName());
        if (value == null) {
            String field = property.getJsonName();
            value = map.get(upperCase(field));
        }

        return value;
    }

    private String upperCase(String field) {
        return Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }

    public Object getTarget() {
        return objectStack.getLast();
    }

    public Object getSource() {
        return jsonStack.getLast();
    }

    public Object bindPrimitive(Object value, Class clazz) {
        if (value.getClass() == clazz) {
            return value;
        } else if (value instanceof Number) {
            if (clazz.equals(Double.class)) {
                return ((Number) value).doubleValue();
            } else if (clazz.equals(Integer.class)) {
                return ((Number) value).intValue();
            } else if (clazz.equals(Long.class)) {
                return ((Number) value).longValue();
            } else if (clazz.equals(Short.class)) {
                return ((Number) value).shortValue();
            } else if (clazz.equals(Byte.class)) {
                return ((Number) value).byteValue();
            } else if (clazz.equals(Float.class)) {
                return ((Number) value).floatValue();
            } else if (clazz == Date.class) {
                return new Date(((Number) value).longValue());
            }
        } else if (value instanceof Boolean && clazz.equals(Boolean.class)) {
            return value;
        }
        throw new JSONException(String.format("%s: Don't know how to bind %s into class %s.  You might need to use an ObjectFactory instead of a plain class.", getCurrentPath().toString(), value, clazz.getName()));
    }

    public Class findClassAtPath(Path currentPath) throws ClassNotFoundException {
        ObjectFactory factory = pathFactories.get(currentPath);
        if (factory instanceof ClassLocatorObjectFactory) {
            return ((ClassLocatorObjectFactory) factory).getLocator().locate(this, currentPath);
        } else {
            return null;
        }
    }
}
