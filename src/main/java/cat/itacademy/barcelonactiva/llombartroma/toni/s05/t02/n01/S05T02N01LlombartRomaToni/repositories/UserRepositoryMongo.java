package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.List;
import java.util.Optional;

public interface UserRepositoryMongo extends MongoRepository<User,Integer> {
    Optional <User> findById(int id);
    Optional<User> findByEmail(String email);

    List<User> findAll();


}
