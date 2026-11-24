package forum.dao.repositories;

import org.springframework.data.repository.CrudRepository;

import forum.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByUsernameIgnoreCase(String username);
    boolean existsByEmailIgnoreCase(String email);

    User findByUsernameIgnoreCase(String username);
    User findByEmailIgnoreCase(String email);
}