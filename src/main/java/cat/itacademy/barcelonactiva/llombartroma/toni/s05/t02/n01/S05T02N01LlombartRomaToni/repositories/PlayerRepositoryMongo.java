package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Player;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;
import java.util.Optional;

public interface PlayerRepositoryMongo extends MongoRepository<Player,String> {
    Optional<List<Player>> getPlayersByName(String name);
    Optional <Player> findById(String id);
    void deletePlayerById(String id);

}
