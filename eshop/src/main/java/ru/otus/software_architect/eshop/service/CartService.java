package ru.otus.software_architect.eshop.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import retrofit2.Response;
import ru.otus.software_architect.eshop.controllers.CartController;
import ru.otus.software_architect.eshop.model.Cart;
import ru.otus.software_architect.eshop.model.ProductEntry;

@Service
public class CartService {
    private static Logger logger = LogManager.getLogger(CartController.class);

    public Cart addProductToCart(ProductEntry productEntry) throws Exception {
        logger.info("addProductToCart: " + productEntry);
        Response<Cart> response =  TransportService.addProductToCart(productEntry);
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new ResponseStatusException(HttpStatus.resolve(response.code()), response.message());
        }
    }

    /*-
    @Async()
    public Cart addProductToCartAsync(ProductEntry productEntry) throws Exception {
        logger.info("addProductToCart: " + productEntry);
        CompletableFuture<Response<Cart>> responseCartFuture = CompletableFuture.supplyAsync(() -> {
            Response<Cart> response = null;
            try {
                response = TransportService.addProductToCart(productEntry);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
            return response;
        });
        Response<Cart> response = responseCartFuture.get(); // block
        logger.info("addProductToCart: after_responseCartFuture_get()");
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new ResponseStatusException(HttpStatus.resolve(response.code()), response.message());
        }
    }
     */

}
