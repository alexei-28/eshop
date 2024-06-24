package ru.otus.software_architect.eshop_orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.software_architect.eshop_orders.api.model.Cart;
import ru.otus.software_architect.eshop_orders.api.model.Product;
import ru.otus.software_architect.eshop_orders.repo.CartRepository;
import ru.otus.software_architect.eshop_orders.util.GsonUtil;

import java.util.*;

@RestController
public class CartController {
    @Autowired
    private CartRepository cartRepository;

    private static Logger logger = LogManager.getLogger(CartController.class);

    /*--
     Use: Use: http://localhost:8092/api/v1/cart?user_name=admin@admin.com
     Get cart by user name.
     */
    @GetMapping("/cart")
    public Cart getCart(@RequestParam(name = "user_name") String user_name) {
        Cart findCart = cartRepository.findByUsername(user_name);
        logger.info("getCart: user_name = " + user_name + " -> findCart:\n" + findCart);
        return findCart;
    }

    /*--
     Use: http://localhost:8092/api/v1/cart/product
     Add product to cart.
     */
    @PostMapping("/cart/product")
    public Cart addProductToCart(@RequestBody Map<String, Object> payloadMap) {
        logger.info("addProductToCart: payloadMap:\n" + payloadMap);
        String userName = payloadMap.get("user_name").toString();
        final String productString = payloadMap.get("product").toString();
        Product product;
        Object fromValue = payloadMap.get("product");
        if (fromValue instanceof LinkedHashMap) {
            product = new ObjectMapper().convertValue(fromValue, Product.class);
        } else {
            product = GsonUtil.gson.fromJson(productString, Product.class);
        }
        int quantity = (int) payloadMap.get("quantity");
        Cart findCart = cartRepository.findByUsername(userName);
        logger.info("addProductToCart: user_name = " + userName + " -> findCart:\n" + findCart);
        if (findCart == null) {
            Cart cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreated(new Date());
            cart.setUsername(userName);
            cart.addProduct(product, quantity);
            cart = cartRepository.save(cart);
            logger.info("addProductToCart: success_add_product_to_new_cart:\n" + cart);
            return cart;
        } else {
            findCart.addProduct(product, quantity);
            findCart = cartRepository.save(findCart);
            logger.info("addProductToCart: success_add_product_to_exist_cart:\n" + findCart);
            return findCart;
        }
    }

    /*--
        Use: http://localhost:8092/api/v1/cart/product?cart_id=3&product_id=1&quantity=1
        Update product's quantity in cart.
    */
    @PutMapping("/cart/product")
    public Cart updateProductInCart(
            @RequestParam(name = "cart_id") int cart_id,
            @RequestParam(name = "product_id") int product_id,
            @RequestParam(name = "quantity") int quantity) {
        logger.info("updateProduct: cart_id: " + cart_id + ", product_id = " + product_id + ", quantity = " + quantity);
        if (quantity <= 0) {
            String error = "Qunatity must be > 0";
            logger.warn("updateProductInCart: error: " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
        Optional<Cart> findCart = cartRepository.findById(cart_id);
        if (findCart.isPresent()) {
            Cart cart = findCart.get();
            cart.setUpdated(new Date());
            cart.updateProductQuantity(product_id, quantity);
            cartRepository.save(cart);
            logger.info("updateProductInCart: success_update_product_in_exist_cart: " + cart);
            return cart;
        } else {
            String error = "Not found cart with id = " + cart_id;
            logger.info("updateProductInCart: error: " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
    }

    // Delete all products(by product_id) from cart
    // Use: http://localhost:8092/api/v1/cart/product?cart_id=3&product_id=2
    @DeleteMapping("/cart/product")
    public void deleteProductFromCart(@RequestParam(name = "cart_id") int cartId, @RequestParam(name = "product_id") int productId) {
        logger.info("deleteProductFromCart: cartId = " + cartId + ", productId = " + productId);
        Optional<Cart> findCart = cartRepository.findById(cartId);
        if (findCart.isPresent()) {
            Cart cart = findCart.get();
            cart.removeProduct(productId);
            cartRepository.save(cart);
            logger.info("deleteProductFromCart: success delete product by id = " + productId);
        } else {
            String error = "Not found cart by id = " + cartId;
            logger.warn("deleteProductFromCart: " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
    }

    // Use: http://localhost:8092/api/v1/cart/1
    // Get cart details
    @GetMapping("/cart/{cart_id}")
    public Cart getCart(@PathVariable(name = "cart_id") int cartId) {
        logger.info("getCart: cartId = " + cartId);
        Optional<Cart> findCart = cartRepository.findById(cartId);
        logger.info("getCart: findCart = " + findCart);
        if (findCart.isPresent()) {
            Cart cart = findCart.get();
            logger.info("getCart: success_find_cart: " + cart);
            return cart;
        } else {
            String error = "Not found cart by id = " + cartId;
            logger.warn("getCart: " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
    }

    // Use: http://localhost:8092/api/v1/carts
    // Get all carts details
    @GetMapping("/carts")
    public Iterable<Cart> getCarts() {
        logger.info("getCarts:");
        Iterable<Cart> allCarts = cartRepository.findAll();
        logger.info("getCarts: allCarts:\n" + allCarts);
        return allCarts;
    }

    // Use: http://localhost:8092/api/v1/cart/1
    // Delete cart
    @DeleteMapping("/cart/{cart_id}")
    public void deleteCart(@PathVariable(name = "cart_id") int cartId) {
        logger.info("deleteCart: cartId = " + cartId);
        Optional<Cart> findCart = cartRepository.findById(cartId);
        logger.info("deleteCart: findCart = " + findCart);
        if (findCart.isPresent()) {
            cartRepository.deleteById(cartId);
            logger.info("deleteCart: success_delete_cart_with_id: " + findCart.get().getId());
        } else {
            String error = "Not found cart by id = " + cartId;
            logger.warn("deleteCart: " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
    }

    // Use: http://localhost:8092/api/v1/carts
    // Delete cart
    @DeleteMapping("/carts")
    public void deleteAllCarts() {
        logger.info("deleteAllCarts: ");
        cartRepository.deleteAll();
        logger.info("deleteAllCarts: success_delete_all_carts");
    }

}
