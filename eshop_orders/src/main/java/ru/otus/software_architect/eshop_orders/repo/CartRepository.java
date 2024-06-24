package ru.otus.software_architect.eshop_orders.repo;

import org.springframework.data.repository.CrudRepository;
import ru.otus.software_architect.eshop_orders.api.model.Cart;

public interface CartRepository extends CrudRepository<Cart, Integer> {

    Cart findByUsername(String username);

}
