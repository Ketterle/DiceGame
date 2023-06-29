package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryMySQL extends JpaRepository<User, Integer> {
    Optional<List<User>> getUsersByName(String name);

    List<User> findAll();
    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);
    void deleteById(int id);
}
