package ru.otus.software_architect.eshop.api;

public class ApiHelper {
    public static final String API_BASE_URL_STUB = "http://127.0.0.1:8081/";
    public static final String API_BASE_URL_NOTIFY = "http://127.0.0.1:8091/";
    public static final String API_BASE_URL_ORDERS = "http://127.0.0.1:8092/";

    public static final String COMMON_METHOD_PREFIX = "api/v1/";

    public static final String ORDER_CART_ADD_PRODUCT = createFullMetodName(API_BASE_URL_ORDERS, "cart/product");

    private static String createFullMetodName(String baseUrl, String methodName) {
        return baseUrl + COMMON_METHOD_PREFIX + methodName;
    }
}
