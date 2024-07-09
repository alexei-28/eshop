package ru.otus.software_architect.eshop.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.otus.software_architect.eshop.api.RestClient;
import ru.otus.software_architect.eshop.api.RestClientFactory;
import ru.otus.software_architect.eshop.model.Cart;
import ru.otus.software_architect.eshop.model.NotifyActionEnum;
import ru.otus.software_architect.eshop.model.ProductEntry;
import ru.otus.software_architect.eshop.util.GsonUtil;

import java.io.IOException;

import static ru.otus.software_architect.eshop.api.ApiHelper.ORDER_CART_ADD_PRODUCT;


public class TransportService {
    private static RestClient restClient = RestClientFactory.createRestClient(RestClient.class);
    private static Logger logger = LogManager.getLogger(TransportService.class);
    private static String currentUserName;

    static {
        logger.info("INIT: ");
        currentUserName = UserService.getCurrentUserName();
    }

    public static void sayHello(Callback<JsonElement> callback) {
        logger.info("getDictionarySet: ");
        Call<JsonElement> call = restClient.sayHello();
        // asynchronously
        call.enqueue(callback);
    }

    public static void notifyByEmail(String email, NotifyActionEnum action, int orderId, Callback<JsonElement> callback) {
        logger.info("notifyByEmail: email = " + email);
        Call<JsonElement> call = restClient.notifyEmail(email, action.name().toLowerCase(), orderId);
        // asynchronously
        call.enqueue(callback);
    }

    public static Response<Cart> addProductToCart(ProductEntry productEntry) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("user_name", currentUserName);
        json.addProperty("product", GsonUtil.gson.toJson(productEntry.getProduct()));
        json.addProperty("quantity", productEntry.getQuantity());
        logger.info("addProductToCart: json = " + json);
        Call<Cart> call = restClient.addProductToCart(ORDER_CART_ADD_PRODUCT, json);
        // sync call
        return call.execute();
    }

    public static void addProductToCartAsync(ProductEntry productEntry, Callback<Cart> callback) {
        JsonObject json = new JsonObject();
        json.addProperty("user_name", currentUserName);
        json.addProperty("product", GsonUtil.gson.toJson(productEntry.getProduct()));
        json.addProperty("quantity", productEntry.getQuantity());
        logger.info("addProductToCart: json = " + json);
        Call<Cart> call = restClient.addProductToCart(ORDER_CART_ADD_PRODUCT, json);
        /// asynchronously
        call.enqueue(callback);
    }

    /*-
    public static void sendMessageToMessageBroker(JsonElement message) {
        logger.info("sendMessageToMessageBroker: message = " + message);
        // Get JMS template bean reference
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        jmsTemplate.convertAndSend(ESHOP_QUEUE, message.toString());
    }

     */
}
