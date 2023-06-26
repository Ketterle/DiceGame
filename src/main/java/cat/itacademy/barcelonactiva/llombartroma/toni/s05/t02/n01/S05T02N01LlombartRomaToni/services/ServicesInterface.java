package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.UserDTO;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public interface ServicesInterface {
    Optional<UserDTO> add(User user);
    Optional<UserDTO> update(String name, int id);
    Optional<Game> newGame(int id);
    default Optional<List<GameDTO>> getPlayerGames(int id) {
        return Optional.empty();
    }
    default Optional<List<UserDTO>> getAllPlayers() {
        return Optional.empty();
    }
    Optional<UserDTO> delete(int id);
    default Optional<List<UserDTO>> playersRanking() {
        return Optional.empty();
    }
    default OptionalDouble averageSuccess() {
        return null;
    }
    default Optional<UserDTO> bestPlayer() {
        return Optional.empty();
    }
    default Optional<UserDTO> worstPlayer() {
        return Optional.empty();
    }


}
