package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
public class ServiceToController implements ServicesInterface {
    private final DiceGameServicesInterfaceMYSQL diceGameServicesMYSQL;

    public ServiceToController(DiceGameServicesInterfaceMYSQL diceGameServicesMYSQL) {
        this.diceGameServicesMYSQL = diceGameServicesMYSQL;
    }
    public Optional<UserDTO> add(User user) {
        return diceGameServicesMYSQL.add(user);
    }
    public Optional<UserDTO> update(String name, int id) {
        try {
            Optional<UserDTO> playerToBeUpdated = diceGameServicesMYSQL.update(name, id);
            if(playerToBeUpdated.isEmpty()) {
                throw new PlayerNotFoundException();
            }
            return playerToBeUpdated;
        }
        catch (PlayerNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    public Optional<Game> newGame(int id) {
        return diceGameServicesMYSQL.newGame(id);
    }
    public Optional<List<GameDTO>> getPlayerGames(int id) {
        return diceGameServicesMYSQL.getPlayerGames(id);
    }
    public Optional<List<UserDTO>> getAllPlayers() {
        return diceGameServicesMYSQL.getAllPlayers();
    }

    public Optional<UserDTO> delete (int id) {
        return diceGameServicesMYSQL.delete(id);
    }

    public Optional<List<UserDTO>> playersRanking() {
        return diceGameServicesMYSQL.playersRanking();
    }

    public OptionalDouble averageSuccess() {
        return diceGameServicesMYSQL.averageSuccess();
    }

    public Optional<UserDTO> bestPlayer() {
        return diceGameServicesMYSQL.bestPlayer();
    }
    public Optional<UserDTO> worstPlayer() {
        return diceGameServicesMYSQL.worstPlayer();
    }

}
