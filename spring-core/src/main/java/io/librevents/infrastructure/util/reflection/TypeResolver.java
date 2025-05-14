package io.librevents.infrastructure.util.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class TypeResolver {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getGenericTypeClass(T instance, int position) {
        ParameterizedType superClass =
                (ParameterizedType) instance.getClass().getGenericSuperclass();
        Type type = superClass.getActualTypeArguments()[position];

        if (type instanceof Class<?>) {
            return (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) type).getRawType();
        }
        return null;
    }
}
