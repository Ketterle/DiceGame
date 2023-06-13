package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Player;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public interface ServicesInterface {
    Optional<PlayerDTO> add(Player player);
    Optional<PlayerDTO> update(String name, String id);
    Optional<Game> newGame(String id);
    default Optional<List<GameDTO>> getPlayerGames(String id) {
        return Optional.empty();
    }
    default Optional<List<PlayerDTO>> getAllPlayers() {
        return Optional.empty();
    }
    Optional<PlayerDTO> delete(String id);
    default Optional<List<PlayerDTO>> playersRanking() {
        return Optional.empty();
    }
    default OptionalDouble averageSuccess() {
        return null;
    }
    default Optional<PlayerDTO> bestPlayer() {
        return Optional.empty();
    }
    default Optional<PlayerDTO> worstPlayer() {
        return Optional.empty();
    }


}
