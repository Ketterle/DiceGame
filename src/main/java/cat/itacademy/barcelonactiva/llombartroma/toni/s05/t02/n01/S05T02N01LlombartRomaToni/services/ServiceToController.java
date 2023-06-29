package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.auth.AuthenticationResponse;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
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
    private final DiceGameServices diceGameServices;



    public ServiceToController(DiceGameServices diceGameServices) {
        this.diceGameServices = diceGameServices;
    }

    public AuthenticationResponse register (User user) {
        try {
            return diceGameServices.register(user);
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public AuthenticationResponse authenticate (User user) {
        try {
           return diceGameServices.authenticate(user);
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<PlayerDTO> update(String name, int id) {
        Optional<PlayerDTO> userDTOOptional;
        try {
            if((userDTOOptional= diceGameServices.update(name, id)).isEmpty()) {
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
        return diceGameServices.newGame(id);
    }
    public Optional<List<GameDTO>> getPlayerGames(int id) {
        return diceGameServices.getPlayerGames(id);
    }
    public List<PlayerRankingDTO> retrieveAllPlayers() {
        return diceGameServices.retrieveAllPlayers();
    }

    public Optional<PlayerDTO> delete (int id) {
        return diceGameServices.delete(id);
    }

    public Optional<List<PlayerRankingDTO>> playersRanking() {
        return diceGameServices.playersRanking();
    }

    public OptionalDouble averageSuccess() {
        return diceGameServices.averageSuccess();
    }

    public Optional<PlayerRankingDTO> bestPlayer() {
        return diceGameServices.bestPlayer();
    }
    public Optional<PlayerRankingDTO> worstPlayer() {
        return diceGameServices.worstPlayer();
    }

}
