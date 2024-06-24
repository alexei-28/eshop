package ru.otus.software_architect.eshop_notify.util;

import com.google.gson.*;

public class GsonUtil {
    public static GsonBuilder gsonbuilder = new GsonBuilder();
    public static Gson gson;
    public static JsonParser parser = new JsonParser();

    static {
        gson = gsonbuilder.create();
    }
}
