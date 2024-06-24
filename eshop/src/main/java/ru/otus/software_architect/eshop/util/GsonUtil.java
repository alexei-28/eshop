package ru.otus.software_architect.eshop.util;

import com.google.gson.*;
import ru.otus.software_architect.eshop.annotation.Exclude;

public class GsonUtil {
    public static GsonBuilder gsonbuilder = new GsonBuilder();
    public static Gson gson;
    public static JsonParser parser = new JsonParser();

    static {
        // @Exclude -> to exclude specific field when serialize/deserilaize
        gsonbuilder.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes field) {
                return field.getAnnotation(Exclude.class) != null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        });
        gson = gsonbuilder.create();
    }
}
