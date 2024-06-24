package ru.otus.software_architect.eshop_orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.otus.software_architect.eshop_orders.api.model.*;
import ru.otus.software_architect.eshop_orders.repo.CartRepository;
import ru.otus.software_architect.eshop_orders.repo.OrdersRepository;
import ru.otus.software_architect.eshop_orders.repo.ProductRepository;
import ru.otus.software_architect.eshop_orders.util.GsonUtil;

import java.util.*;

@RestController
public class OrderController {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private ProductRepository productRepository;
    private static int PROMO_CODE_DISCOUNT_AMOUNT = 2;

    private static Logger logger = LogManager.getLogger(OrderController.class);

    // Use: http://localhost:8092/api/v1/orders
    // Get all orders details
    @GetMapping("/orders")
    public Iterable<Orders> getOrders() {
        logger.info("getOrders:");
        Iterable<Orders> allOrders = ordersRepository.findAll();
        return allOrders;
    }

    // Use: http://localhost:8092/api/v1/order/1
    // Get order details
    @GetMapping("/order/{order_id}")
    public Orders getOrder(@PathVariable(name = "order_id") int order_id) {
        logger.info("getOrder: order_id = " + order_id);
        Optional<Orders> findOrders = ordersRepository.findById(order_id);
        logger.info("getOrder: findOrders = " + findOrders);
        if (findOrders.isPresent()) {
            Orders order = findOrders.get();
            logger.info("getOrder: success_find_order: " + order);
            return order;
        } else {
            String error = "Not found order by id = " + order_id;
            logger.warn("getOrder: " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
    }

    /*-
      Use: http://localhost:8092/api/v1/order/cart
      Create order from whole cart:
     */
    @PostMapping("/order/cart")
    public Orders createOrderByCart(@RequestBody Map<String, Object> payloadMap) {
        int cartId = (int) payloadMap.get("cart_id");
        Optional<Cart> findCart = cartRepository.findById(cartId);
        if (findCart.isPresent()) {
            Orders order;
            final String orderString = payloadMap.get("order").toString();
            Object fromValue = payloadMap.get("order");
            if (fromValue instanceof LinkedHashMap) {
                order = new ObjectMapper().convertValue(fromValue, Orders.class);
            } else {
                order = GsonUtil.gson.fromJson(orderString, Orders.class);
            }
            order.setCreated(new Date());
            Cart cart = findCart.get();
            order.setUsername(cart.getUsername());
            Set<ProductEntry> productEntitiesInCartSet = cart.getProductEntities();
            for (ProductEntry productEntry : productEntitiesInCartSet) {
                order.addProduct(productEntry.getProduct(), productEntry.getQuantity());
            }
            double orderTotalAmount = cart.getTotalAmount();
            if (order.getPromoCode() != null && orderTotalAmount > PROMO_CODE_DISCOUNT_AMOUNT) {
                orderTotalAmount = orderTotalAmount - PROMO_CODE_DISCOUNT_AMOUNT;
            }
            order.setTotalAmount(orderTotalAmount);
            order.setCurrenty(cart.getCurrency());
            ordersRepository.save(order);
            return order;
        } else {
            String error = "Not found cart with id " + cartId;
            logger.warn(error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
    }

    /*-
    Use: http://localhost:8092/api/v1/order/product
    Create order by product
    */
    @PostMapping("/order/product")
    public Orders addProductToOrder(@RequestBody Map<String, Object> payloadMap) {
        logger.info("addProductToOrder: payloadMap: " + payloadMap);
        String userName = payloadMap.get("user_name").toString();
        String paymentCardNumber = payloadMap.get("paymentCardNumber").toString();
        final Product product = new ObjectMapper().convertValue(payloadMap.get("product"), Product.class);
        int quantity = (int) payloadMap.get("quantity");
        Orders findOrder = ordersRepository.findByUsername(userName);
        if (findOrder == null) {
            Orders order = new Orders();
            order.setCreated(new Date());
            order.setUsername(userName);
            order.addProduct(product, quantity);
            ordersRepository.save(order);
            logger.info("addProductToOrder: success_add_product_to_new_order: " + order);
            return order;
        } else {
            findOrder.addProduct(product, quantity);
            ordersRepository.save(findOrder);
            logger.info("addProductToOrder: success_add_product_to_exist_order: " + findOrder);
            return findOrder;
        }
    }

    /*--
        Use: http://localhost:8092/api/v1/order/product?order_id=1&product_id=10&quantity=10
        Update product's quantity in order.
    */
    @PutMapping("/order/product")
    public Orders updateProductInOrder(
            @RequestParam(name = "order_id") int order_id,
            @RequestParam(name = "product_id") int product_id,
            @RequestParam(name = "quantity") int quantity) {
        logger.info("updateProductInOrder: order_id: " + order_id + ", product_id = " + product_id + ", quantity = " + quantity);
        if (quantity <= 0) {
            String error = "Qunatity must be > 0";
            logger.error("updateProudct: error = " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
        Optional<Orders> findOrder = ordersRepository.findById(order_id);
        if (findOrder.isPresent()) {
            Orders order = findOrder.get();
            order.setUpdated(new Date());
            order.updateProductQuantity(product_id, quantity);
            ordersRepository.save(order);
            logger.info("updateProductInOrder: success_update_product_in_exist_order: " + order);
            return order;
        } else {
            String error = "Not found order with id = " + order_id;
            logger.info("updateProductInOrder: error: " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
    }

    // Delete all products(by product_id) from order
    // Use: http://localhost:8092/api/v1/order/product?order_id=8&product_id=10
    @DeleteMapping("/order/product")
    public void deleteProductFromOrder(@RequestParam(name = "order_id") int orderId, @RequestParam(name = "product_id") int productId) {
        logger.info("deleteProductFromOrder: orderId = " + orderId + ", productId = " + productId);
        Optional<Orders> findOrder = ordersRepository.findById(orderId);
        if (findOrder.isPresent()) {
            Orders order = findOrder.get();
            order.removeProduct(productId);
            logger.info("deleteProductFromOrder: success_delete_order_with_id: " + order.getId());
        } else {
            String error = "Not found order by id = " + orderId;
            logger.warn("deleteProductFromOrder: " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
    }

    // Use: http://localhost:8092/api/v1/order/1
    // Delete order
    @DeleteMapping("/order/{order_id}")
    public void deleteOrder(@PathVariable(name = "order_id") int orderId) {
        logger.info("deleteOrder: order_id = " + orderId);
        Optional<Orders> findOrder = ordersRepository.findById(orderId);
        logger.info("deleteOrder: findOrder = " + findOrder);
        if (findOrder.isPresent()) {
            ordersRepository.deleteById(orderId);
            logger.info("deleteOrder: success_delete_delete_with_id: " + findOrder.get().getId());
        } else {
            String error = "Not found order by id = " + orderId;
            logger.warn("deleteOrder: " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
    }

    // Use: http://localhost:8092/api/v1/orders
    // Delete orders
    @DeleteMapping("/orders")
    public void deleteAllOrders() {
        logger.info("deleteAllOrders: ");
        ordersRepository.deleteAll();
        logger.info("deleteAllOrders: success_delete_all_orders");
    }

    /*-
    Use: http://localhost:8092/api/v1/order/shipping
    Add shipping information.
   */
    @PostMapping("/order/shipping")
    public Orders addShippingToOrder(@RequestBody Map<String, Object> payloadMap) {
        logger.info("addShippingToOrder: payloadMap: " + payloadMap);
        String userName = payloadMap.get("user_name").toString();
        final Shipping shipping = new ObjectMapper().convertValue(payloadMap.get("shipping"), Shipping.class);
        Orders findOrder = ordersRepository.findByUsername(userName);
        if (findOrder == null) {
            Orders order = new Orders();
            order.setCreated(new Date());
            order.setUsername(userName);
            order.setShipping(shipping);
            order = ordersRepository.save(order);
            logger.info("addShippingToOrder: success_add_shipping_to_new_order: " + order);
            return order;
        } else {
            if (findOrder.getShipping() != null) {
                String error = "Shipping already exist. Order id = " + findOrder.getId();
                logger.warn("addShippingToOrder: " + error);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
            }
            findOrder.setShipping(shipping);
            findOrder = ordersRepository.save(findOrder);
            logger.info("addShippingToOrder: success_add_shipping_to_exist_order: " + findOrder);
            return findOrder;
        }
    }

    /*--
       Use: http://localhost:8092/api/v1/order/shipping/order_id=1
       Update shipping information.
   */
    @PutMapping("/order/shipping")
    public Orders updateShippingInOrder(@RequestBody Map<String, Object> payloadMap) {
        logger.info("updateShippingInOrder:");
        int order_id = (int) payloadMap.get("order_id");
        Optional<Orders> findOrder = ordersRepository.findById(order_id);
        if (findOrder.isPresent()) {
            Orders order = findOrder.get();
            order.setUpdated(new Date());
            final Shipping shipping = new ObjectMapper().convertValue(payloadMap.get("shipping"), Shipping.class);
            order.setShipping(shipping);
            ordersRepository.save(order);
            logger.info("updateShippingInOrder: success_update_product_in_exist_order: " + order);
            return order;
        } else {
            String error = "Not found order with id = " + order_id;
            logger.warn("updateShippingInOrder: error: " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
    }

    /*--
    Use: http://localhost:8092/api/v1/order/promocode?order_id=10&promocode_id=ABC1
    Add promocode.
    */
    @PutMapping("/order/promocode")
    public Orders addPromocodeToOrder(@RequestParam(name = "order_id") int order_id, @RequestParam(name = "promoCode") String promoCode) {
        logger.info("addPromocodeToOrder: order_id = " + order_id);
        Optional<Orders> findOrder = ordersRepository.findById(order_id);
        if (!findOrder.isPresent()) {
            String error = "Not found order by id = " + order_id;
            logger.error("addPromocodeToOrder: error = " + error);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }
        Orders order = findOrder.get();
        order.setPromoCode(promoCode);
        order = ordersRepository.save(order);
        return order;
    }

}
