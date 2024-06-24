package ru.otus.software_architect.eshop_orders.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.software_architect.eshop_orders.api.model.Product;
import ru.otus.software_architect.eshop_orders.repo.ProductRepository;

@RestController
public class ProductController {
    @Autowired
    private ProductRepository productRepository;

    private static Logger logger = LogManager.getLogger(ProductController.class);

    // Use: http://localhost:8092/api/v1/products
    // Get all products
    @GetMapping("/products")
    public Iterable<Product> getProducts() {
        logger.info("getProducts:");
        Iterable<Product> allProducts = productRepository.findAll();
        return allProducts;
    }
}
