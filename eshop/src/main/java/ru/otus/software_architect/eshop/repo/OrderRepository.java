package ru.otus.software_architect.eshop.repo;

import ru.otus.software_architect.eshop.model.Orders;
import org.springframework.data.repository.CrudRepository;
import ru.otus.software_architect.eshop.model.User;

import java.util.List;

public interface OrderRepository extends CrudRepository<Orders, Integer> {

    // Spring Data - use JPQL -> generate SQL query on runtime
    public List<Orders> findByName(String name);

    public List<Orders> findByNameOrderById(String name);

    public List<Orders> findByUser(User user);

}
