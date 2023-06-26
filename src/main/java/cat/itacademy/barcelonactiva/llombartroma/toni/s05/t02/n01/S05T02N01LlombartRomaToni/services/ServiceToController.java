package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Service
public class ServiceToController implements ServicesInterface {
    private final DiceGameServicesMYSQL diceGameServicesMYSQL;

    public ServiceToController(DiceGameServicesMYSQL diceGameServicesMYSQL) {
        this.diceGameServicesMYSQL = diceGameServicesMYSQL;
    }
    public Optional<PlayerDTO> update(String name, int id) {
        Optional<PlayerDTO> userDTOOptional;
        try {
            if((userDTOOptional=diceGameServicesMYSQL.update(name, id)).isEmpty()) {
                    throw new PlayerNotFoundException();
            }
        }
        catch (PlayerNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        }
        return userDTOOptional;
    }
    public Optional<Game> newGame(int id) {
        return diceGameServicesMYSQL.newGame(id);
    }
    public Optional<List<GameDTO>> getPlayerGames(int id) {
        return diceGameServicesMYSQL.getPlayerGames(id);
    }
    public Optional<List<PlayerDTO>> getAllPlayers() {
        return diceGameServicesMYSQL.getAllPlayers();
    }

    public Optional<PlayerDTO> delete (int id) {
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
