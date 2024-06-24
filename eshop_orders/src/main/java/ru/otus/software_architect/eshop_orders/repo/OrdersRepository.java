package ru.otus.software_architect.eshop_orders.repo;

import org.springframework.data.repository.CrudRepository;
import ru.otus.software_architect.eshop_orders.api.model.Orders;

public interface OrdersRepository extends CrudRepository<Orders, Integer> {

    Orders findByUsername(String username);
}
