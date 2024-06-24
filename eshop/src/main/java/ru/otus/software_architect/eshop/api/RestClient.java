package ru.otus.software_architect.eshop.api;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.*;
import ru.otus.software_architect.eshop.model.Cart;

public interface RestClient {

    @GET("api/v1/hello")
    Call<JsonElement> sayHello();

    @POST("api/v1/notifyEmail")
    Call<JsonElement> notifyEmail(@Query("emailTo") String user,
                                  @Query("action") String action,
                                  @Query("orderId") int orderId);

    @POST
    Call<Cart> addProductToCart(@Url String url, @Body JsonElement body);
}
