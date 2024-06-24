package ru.otus.software_architect.eshop_orders.util;


import com.google.gson.*;

public class GsonUtil {
    public static GsonBuilder gsonbuilder = new GsonBuilder();
    public static Gson gson;
    public static JsonParser parser = new JsonParser();

    static {
        gsonbuilder.setPrettyPrinting();
        gson = gsonbuilder.create();
    }
}
