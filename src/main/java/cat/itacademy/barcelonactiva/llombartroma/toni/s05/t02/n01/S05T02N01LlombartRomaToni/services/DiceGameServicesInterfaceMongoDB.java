package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Player;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.PlayerRepositoryMongo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DiceGameServicesInterfaceMongoDB implements ServicesInterface {

    private final PlayerRepositoryMongo playerRepositoryMongo;

    public DiceGameServicesInterfaceMongoDB(PlayerRepositoryMongo playerRepositoryMongo) {
        this.playerRepositoryMongo = playerRepositoryMongo;
    }
    public Optional<PlayerDTO> add(Player player) {
        Optional<List<Player>> playerRetrievedOptional = playerRepositoryMongo.getPlayersByName(player.getName());
        if (!playerRetrievedOptional.get().isEmpty() && playerRetrievedOptional.get().stream().noneMatch(s->s.getName().equals((Player.DEFAULT_NAME)))) {
            return Optional.empty();
        } else {
            player.setId(idGeneratorMongoDB(playerRepositoryMongo));
            return Optional.of(Player.fromPlayerToPlayerDTO(playerRepositoryMongo.save(player)));
        }
    }

    public Optional<PlayerDTO> update(String name, String id) {
        Optional<Player> optionalPlayer = playerRepositoryMongo.findById(id);
        Player updatePlayer = optionalPlayer.get();
        updatePlayer.setName(name);
        return Optional.of(Player.fromPlayerToPlayerDTO(playerRepositoryMongo.save(updatePlayer)));
    }

    public Optional<Game> newGame(String id) {
        Player player = playerRepositoryMongo.findById(id).orElseThrow(() -> new IllegalArgumentException("Player not found"));
        Game game = new Game();
        player.getGames().add(game);
        game.setPlayer(null);
        playerRepositoryMongo.save(player);
        return Optional.of(game);
    }

    public Optional<PlayerDTO> delete(String id) {
        Optional<Player> optionalPlayer = playerRepositoryMongo.findById(id);
        optionalPlayer.ifPresent(player -> playerRepositoryMongo.deletePlayerById(player.getId()));
        return Optional.of(Player.fromPlayerToPlayerDTO(optionalPlayer.get()));
    }

    public static String idGeneratorMongoDB(PlayerRepositoryMongo playerRepositoryMongo) {
        Optional<Player> playerMaxId = playerRepositoryMongo.findAll().stream()
                .max(Comparator.comparing(s -> Integer.parseInt(s.getId())));
        return playerMaxId.map(player -> String.valueOf(Integer.parseInt(player.getId()) + 1)).orElse("0");
    }

}
