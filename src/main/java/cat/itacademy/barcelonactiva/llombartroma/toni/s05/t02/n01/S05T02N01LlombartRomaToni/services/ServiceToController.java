package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Player;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
public class ServiceToController implements ServicesInterface {
    private final DiceGameServicesInterfaceMYSQL diceGameServicesMYSQL;
    private final DiceGameServicesInterfaceMongoDB diceGameServicesMongoDB;

    public ServiceToController(DiceGameServicesInterfaceMYSQL diceGameServicesMYSQL, DiceGameServicesInterfaceMongoDB diceGameServicesMongoDB) {
        this.diceGameServicesMYSQL = diceGameServicesMYSQL;
        this.diceGameServicesMongoDB = diceGameServicesMongoDB;
    }
    public Optional<PlayerDTO> add(Player player) {
        diceGameServicesMYSQL.add(player);
        return diceGameServicesMongoDB.add(player);
    }
    public Optional<PlayerDTO> update(String name, String id) {
        try {
            Optional<PlayerDTO> playerToBeUpdated = diceGameServicesMYSQL.update(name, id);
            diceGameServicesMongoDB.update(name,id);
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
    public Optional<Game> newGame(String id) {
        diceGameServicesMongoDB.newGame(id);
        return diceGameServicesMYSQL.newGame(id);
    }
    public Optional<List<GameDTO>> getPlayerGames(String id) {
        return diceGameServicesMYSQL.getPlayerGames(id);
    }
    public Optional<List<PlayerDTO>> getAllPlayers() {
        return diceGameServicesMYSQL.getAllPlayers();
    }

    public Optional<PlayerDTO> delete(String id) {
        diceGameServicesMongoDB.delete(id);
        return diceGameServicesMYSQL.delete(id);
    }

    public Optional<List<PlayerDTO>> playersRanking() {
        return diceGameServicesMYSQL.playersRanking();
    }

    public OptionalDouble averageSuccess() {
        return diceGameServicesMYSQL.averageSuccess();
    }

    public Optional<PlayerDTO> bestPlayer() {
        return diceGameServicesMYSQL.bestPlayer();
    }
    public Optional<PlayerDTO> worstPlayer() {
        return diceGameServicesMYSQL.worstPlayer();
    }

}
