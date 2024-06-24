package ru.otus.software_architect.eshop_orders.repo;

import org.springframework.data.repository.CrudRepository;
import ru.otus.software_architect.eshop_orders.api.model.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>  {
}
