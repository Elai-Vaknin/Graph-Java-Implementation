package api;

import com.google.gson.*;

import java.lang.reflect.Type;

/* Interface to translate json string interface objects into classes
* Used for DWGraph_Algo save and load
* */

public class InterfaceSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T> {
    private final Class<T> implementationClass;

    public InterfaceSerializer(final Class<T> implementationClass) {
        this.implementationClass = implementationClass;
    }

    public static <T> InterfaceSerializer<T> interfaceSerializer(final Class<T> implementationClass) {
        return new InterfaceSerializer<>(implementationClass);
    }

    @Override
    public JsonElement serialize(final T value, final Type type, final JsonSerializationContext context) {
        final Type targetType = value != null ? value.getClass() : type;

        return context.serialize(value, targetType);
    }

    @Override
    public T deserialize(final JsonElement jsonElement, final Type typeOfT, final JsonDeserializationContext context) {
        return context.deserialize(jsonElement, implementationClass);
    }
}