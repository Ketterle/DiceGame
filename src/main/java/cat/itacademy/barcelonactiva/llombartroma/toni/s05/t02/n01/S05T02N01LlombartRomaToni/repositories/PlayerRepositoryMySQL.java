package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PlayerRepositoryMySQL extends JpaRepository<Player,String> {
    Optional<List<Player>> getPlayersByName(String name);
    Optional<Player> findById(String id);
    Optional<Player> findByEmail(String email);
    void deleteById(String id);
}
