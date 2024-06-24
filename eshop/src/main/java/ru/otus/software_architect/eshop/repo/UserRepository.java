package ru.otus.software_architect.eshop.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.software_architect.eshop.model.Role;
import ru.otus.software_architect.eshop.model.User;

import java.util.List;
import java.util.Set;

// Use JPQL
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

    List<User> findAllByRolesIn(Set<Role> role);
}
