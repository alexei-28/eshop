package ru.otus.software_architect.eshop.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.software_architect.eshop.model.*;
import ru.otus.software_architect.eshop.repo.CartRepository;
import ru.otus.software_architect.eshop.repo.UserRepository;
import ru.otus.software_architect.eshop.service.CartService;
import ru.otus.software_architect.eshop.service.UserService;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Controller
public class CartController {
    @Value("${spring.application.name}")
    private String appName;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartService cartService;
    private User currentUser;

    private static Logger logger = LogManager.getLogger(CartController.class);

    // Get cart by GET method
    @GetMapping("/cart")
    public String getCartDetails(Model model) {
        currentUser = userRepository.findByUsername(UserService.getCurrentUserName());
        Cart findLocalCart = cartRepository.findByUsername(currentUser.getUsername());
        logger.info("getCartDetails: userName = " + currentUser.getUsername() + " -> findLocalCart:\n" + findLocalCart);
        Set<ProductEntry> productEntities = new HashSet<>();
        if (findLocalCart != null) {
            productEntities = findLocalCart.getProductEntities();
        }
        model.addAttribute("productEntities", productEntities);
        model.addAttribute("appName", appName);
        return "cart";
    }

    @RequestMapping("cart/add")
    public String addProduct(Model model) {
        logger.info("addProduct");
        model.addAttribute("isAdd", true);
        ProductEntry productEntry = new ProductEntry();
        productEntry.setProduct(new Product());
        model.addAttribute("productEntry", productEntry);
        model.addAttribute("title", "Add Product");
        model.addAttribute("viewMode", ViewMode.ADD);
        return "productEntry";
    }

    // Add product to cart by POST method
    @PostMapping(value = "/productEntry")
    public String submitProductEntry(ProductEntry productEntry, Model model) {
        try {
            if (productEntry.getId() == 0) { // add productEntry
                logger.info("submitProductEntry: add_productEntry");
                Product product = productEntry.getProduct();
                product.setProductId(UUID.randomUUID().toString());
                product.setCreated(new Date());
                productEntry.setProduct(product);
                productEntry.setCreated(product.getCreated());
            } else { // update productEntry
                logger.info("submitProductEntry: update_productEntry");
                Product product = productEntry.getProduct();
                product.setUpdated(new Date());
                productEntry.setUpdated(product.getUpdated());
            }
            Cart cart = cartService.addProductToCart(productEntry);
            logger.info("submitProductEntry: success_return_cart_from_response\n " + cart);
            // When you do save on entity with empty id it will do a save(insert).
            // When you do save on entity with existing id it will do an update.
            // The save() method returns the saved entity, including the updated id field.
            Cart findLocalCart = cartRepository.findByUsername(currentUser.getUsername());
            logger.info("submitProductEntry: userName = " + currentUser.getUsername() + " -> findLocalCart:\n" + findLocalCart);
            if (findLocalCart != null) {
                logger.info("submitProductEntry: already_exist_Localcart -> update_Localcart");
                cart.setId(findLocalCart.getId());
            }
            cart = cartRepository.save(cart);
            logger.info("submitProductEntry: success_saved_cart_to_db:\n" + cart);
            Cart testLocalCart = cartRepository.findByUsername(currentUser.getUsername());
            logger.info("submitProductEntry: testLocalCart:\n" + testLocalCart);
            return "redirect:/cart";
        } catch (Exception ex) {
            logger.error("submitProductEntry: Error = " + ex.getMessage(), ex);
            model.addAttribute("addProductError", ex.getMessage());
            return "productEntry.html";
        }
    }
}