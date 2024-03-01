package rosarioscilipoti.backendecomerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rosarioscilipoti.backendecomerce.entites.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
