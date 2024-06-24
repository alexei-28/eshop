package ru.otus.software_architect.eshop;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;
import ru.otus.software_architect.eshop.model.Category;
import ru.otus.software_architect.eshop.repo.CategoryRepository;

@SpringBootTest
public class CategoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void shouldNotAllowToPersistNullProperies() {
        Assertions.assertThrows(TransactionSystemException.class, () -> {
            categoryRepository.save(new Category());
        });
    }
}
