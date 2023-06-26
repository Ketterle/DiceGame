package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public interface ServicesInterface {
    Optional<PlayerDTO> update(String name, int id);
    Optional<Game> newGame(int id);
    default Optional<List<GameDTO>> getPlayerGames(int id) {
        return Optional.empty();
    }
    default Optional<List<PlayerDTO>> getAllPlayers() {
        return Optional.empty();
    }
    Optional<PlayerDTO> delete(int id);
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
