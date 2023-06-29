package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerRankingDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

/* This class acts as an extra layer between service and controller. Very useful when we have more than one service which persists in different DB */
@Service
public final class ServiceToController implements ServicesInterface {
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
    public Optional<List<PlayerRankingDTO>> getAllPlayers() {
        return diceGameServicesMYSQL.getAllPlayers();
    }

    public Optional<PlayerDTO> delete (int id) {
        return diceGameServicesMYSQL.delete(id);
    }

    public Optional<List<PlayerRankingDTO>> playersRanking() {
        return diceGameServicesMYSQL.playersRanking();
    }

    public OptionalDouble averageSuccess() {
        return diceGameServicesMYSQL.averageSuccess();
    }

    public Optional<PlayerRankingDTO> bestPlayer() {
        return diceGameServicesMYSQL.bestPlayer();
    }
    public Optional<PlayerRankingDTO> worstPlayer() {
        return diceGameServicesMYSQL.worstPlayer();
    }

}
