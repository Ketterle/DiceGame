package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Player;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.GameRepositoryMySQL;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.PlayerRepositoryMySQL;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiceGameServicesInterfaceMYSQL implements ServicesInterface {

    private final PlayerRepositoryMySQL playerRepositoryMySQL;
    private final GameRepositoryMySQL gameRepositoryMySQL;

    public DiceGameServicesInterfaceMYSQL(PlayerRepositoryMySQL playerRepositoryMySQL, GameRepositoryMySQL gameRepositoryMySQL) {
        this.playerRepositoryMySQL = playerRepositoryMySQL;
        this.gameRepositoryMySQL = gameRepositoryMySQL;

    }

    /* Implements how API add a new Player */
    public Optional<PlayerDTO> add(Player player) {
        Optional<List<Player>> playerRetrievedOptional = playerRepositoryMySQL.getPlayersByName(player.getName());
        if (!playerRetrievedOptional.get().isEmpty() && playerRetrievedOptional.get().stream().noneMatch(s->s.getName().equals((Player.DEFAULT_NAME)))) {
            return Optional.empty();
        } else {
            player.setId(idGeneratorMySQL(playerRepositoryMySQL));
            return Optional.of(Player.fromPlayerToPlayerDTO(playerRepositoryMySQL.save(player)));
        }
    }

    public Optional<PlayerDTO> update(String name, String id) {
        Optional<Player> optionalPlayer = playerRepositoryMySQL.findById(id);
        Player updatePlayer = optionalPlayer.get();
        updatePlayer.setName(name);
        return Optional.of(Player.fromPlayerToPlayerDTO(playerRepositoryMySQL.save(updatePlayer)));
    }
    public Optional<Game> newGame(String id) {
        try {
            Optional<Player> player = playerRepositoryMySQL.findById(id);
            if(player.isPresent()) {
                Game game = new Game();
                game.setPlayer(player.get());
                player.get().getGames().add(game);
                gameRepositoryMySQL.save(game);
                return Optional.of(game);
            }
            else {
                throw new PlayerNotFoundException();
            }
        }
        catch (PlayerNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public Optional<List<GameDTO>> getPlayerGames(String id) {
        try {
            Optional<Player> player = playerRepositoryMySQL.findById(id);
            if(player.isPresent()) {
                List<GameDTO> games = gameRepositoryMySQL.findAllByPlayerId(id).stream().map(Game::fromGameToGameDTO).collect(Collectors.toList());
                return Optional.of(games);
            }
            else {
                throw new PlayerNotFoundException();
            }
        }
        catch (PlayerNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public Optional<List<PlayerDTO>> getAllPlayers() {
        List<PlayerDTO> playersDTO = playerRepositoryMySQL.findAll().stream().map(Player::fromPlayerToPlayerDTO).collect(Collectors.toList());
        return Optional.of(playersDTO);
    }
    public Optional<PlayerDTO> delete(String id) {
        Optional<Player> optionalPlayer = playerRepositoryMySQL.findById(id);
        try {
            if (optionalPlayer.isPresent()) {
                optionalPlayer.ifPresent(player -> playerRepositoryMySQL.deleteById(player.getId()));
                return Optional.of(Player.fromPlayerToPlayerDTO(optionalPlayer.get()));
            } else {
                throw new PlayerNotFoundException();
            }
        }
        catch(PlayerNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<List<PlayerDTO>> playersRanking() {
        List<PlayerDTO> playersDTO = playerRepositoryMySQL.findAll().stream().map(Player::fromPlayerToPlayerDTO).sorted().collect(Collectors.toList());
        return Optional.of(playersDTO);
    }
    public OptionalDouble averageSuccess() {
        List<PlayerDTO> playersDTO = playerRepositoryMySQL.findAll().stream().map(Player::fromPlayerToPlayerDTO).toList();
        return playersDTO.stream().mapToDouble(PlayerDTO::getSuccessRate).average();
    }
    public Optional<PlayerDTO> bestPlayer() {
        return getAllPlayers().get().stream().sorted().findFirst();

    }
    public Optional<PlayerDTO> worstPlayer() {
        return getAllPlayers().get().stream().max(Comparator.naturalOrder());
    }
    public static String idGeneratorMySQL(PlayerRepositoryMySQL playerRepositoryMySQL) {
        Optional<Player> playerMaxId = playerRepositoryMySQL.findAll().stream()
                .max(Comparator.comparing(s -> Integer.parseInt(s.getId())));
        return playerMaxId.map(player -> String.valueOf(Integer.parseInt(player.getId()) + 1)).orElse("0");
    }
}
