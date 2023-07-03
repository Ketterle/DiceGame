package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerWithGamesDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerWithoutGamesDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/* This interface defines the necessary methods in business logic */
public interface ServicesInterface {
    Optional<PlayerWithGamesDTO> update(String name, int id);
    Optional<Game> newGame(int id);
    default Optional<List<GameDTO>> getPlayerGames(int id) {
        return Optional.empty();
    }
    default List<PlayerWithoutGamesDTO> retrieveAllPlayers() {
        return new ArrayList<>();
    }
    Optional<PlayerWithGamesDTO> delete(int id);
    default Optional<List<PlayerWithoutGamesDTO>> playersRanking() {
        return Optional.empty();
    }
    default OptionalDouble averageSuccess() {
        return OptionalDouble.empty();
    }
    default Optional<PlayerWithoutGamesDTO> bestPlayer() {
        return Optional.empty();
    }
    default Optional<PlayerWithoutGamesDTO> worstPlayer() {
        return Optional.empty();
    }


}
