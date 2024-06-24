package ru.otus.software_architect.eshop.repo;

import org.springframework.data.repository.CrudRepository;
import ru.otus.software_architect.eshop.model.Cart;

public interface CartRepository extends CrudRepository<Cart, Integer> {
    public Cart findByUsername(String userName);
}
