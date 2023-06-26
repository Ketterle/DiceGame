package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.GameRepositoryMySQL;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.UserRepositoryMySQL;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiceGameServicesMYSQL implements ServicesInterface {

    private final UserRepositoryMySQL userRepositoryMySQL;
    private final GameRepositoryMySQL gameRepositoryMySQL;

    public DiceGameServicesMYSQL(UserRepositoryMySQL userRepositoryMySQL, GameRepositoryMySQL gameRepositoryMySQL) {
        this.userRepositoryMySQL = userRepositoryMySQL;
        this.gameRepositoryMySQL = gameRepositoryMySQL;

    }

    public Optional<PlayerDTO> update(String name, int id) {
        Optional<User> optionalPlayer = userRepositoryMySQL.findById(id);
        User updateUser = optionalPlayer.get();
        updateUser.setName(name);
        return Optional.of(fromUserToPlayerDTO(userRepositoryMySQL.save(updateUser)));
    }
    public Optional<Game> newGame(int id) {
        try {
            Optional<User> user = Optional.of(userRepositoryMySQL.findById(id).get());
            if(user.isPresent()) {
                Game game = new Game();
                game.setPlayer(user.get());
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
    public Optional<List<GameDTO>> getPlayerGames(int id) {
        try {
            Optional<User> player = userRepositoryMySQL.findById(id);
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
            List<PlayerDTO> playersDTO = new ArrayList<>();
            List<User> players = userRepositoryMySQL.findAll();
            for(User player:players) {
                List<Game> games = gameRepositoryMySQL.findGamesByPlayer(player).get();
                PlayerDTO playerDTO = fromUserToPlayerDTO(player);
                playerDTO.setGames(games);
                playerDTO.setSuccessRate(playerDTO.successRatePlayerCalculator());
                playersDTO.add(playerDTO);
            }
        return Optional.of(playersDTO);
    }
    public Optional<PlayerDTO> delete(int id) {
        Optional<User> optionalPlayer = userRepositoryMySQL.findById(id);
        try {
            if (optionalPlayer.isPresent()) {
                optionalPlayer.ifPresent(player -> userRepositoryMySQL.deleteById(player.getId()));
                return Optional.of(fromUserToPlayerDTO(optionalPlayer.get()));
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
        List<PlayerDTO> playersDTO = userRepositoryMySQL.findAll().stream().map(this::fromUserToPlayerDTO).sorted().collect(Collectors.toList());
        return Optional.of(playersDTO);
    }
    public OptionalDouble averageSuccess() {
        List<PlayerDTO> playersDTO = userRepositoryMySQL.findAll().stream().map(this::fromUserToPlayerDTO).toList();
        return playersDTO.stream().mapToDouble(PlayerDTO::getSuccessRate).average();
    }
    public Optional<PlayerDTO> bestPlayer() {
        return getAllPlayers().get().stream().sorted().findFirst();

    }
    public Optional<PlayerDTO> worstPlayer() {
        return getAllPlayers().get().stream().max(Comparator.naturalOrder());
    }

    private PlayerDTO fromUserToPlayerDTO(User player) {
        ModelMapper modelMapper = new ModelMapper();
        PlayerDTO playerDTO = modelMapper.map(player, PlayerDTO.class);
        playerDTO.setGames(new ArrayList<>());
        List<Game> games = gameRepositoryMySQL.findGamesByPlayer(player).get();
        playerDTO.setGames(games);
        playerDTO.setSuccessRate(playerDTO.successRatePlayerCalculator());
        return playerDTO;
    }


}
