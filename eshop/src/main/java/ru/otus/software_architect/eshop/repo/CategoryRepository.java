package ru.otus.software_architect.eshop.repo;

import org.springframework.data.repository.CrudRepository;
import ru.otus.software_architect.eshop.model.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    // Spring Data - use JPQL -> generate SQL query on runtime
    public List<Category> findByName(String name);

    public List<Category> findByNameOrderById(String name);
}
