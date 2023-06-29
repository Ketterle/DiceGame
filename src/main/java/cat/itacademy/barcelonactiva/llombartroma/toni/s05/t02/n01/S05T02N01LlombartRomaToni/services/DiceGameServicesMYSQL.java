package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.GameStatus;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerRankingDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.GameRepositoryMySQL;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.UserRepositoryMySQL;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/* This class implements the business logic. Sets how elements are updated and fetched and calculates average scores */
@Service
public final class DiceGameServicesMYSQL implements ServicesInterface {

    private final UserRepositoryMySQL userRepositoryMySQL;
    private final GameRepositoryMySQL gameRepositoryMySQL;

    public DiceGameServicesMYSQL(UserRepositoryMySQL userRepositoryMySQL, GameRepositoryMySQL gameRepositoryMySQL) {
        this.userRepositoryMySQL = userRepositoryMySQL;
        this.gameRepositoryMySQL = gameRepositoryMySQL;

    }
    /* This method updates a player name */
    public Optional<PlayerDTO> update(String name, int id) {
        try {
            Optional<User> optionalPlayer = userRepositoryMySQL.findById(id);
            if(optionalPlayer.isPresent()) {
                User updateUser = optionalPlayer.get();
                updateUser.setName(name);
                return Optional.of(fromUserToPlayerDTO(userRepositoryMySQL.save(updateUser)));
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
    /* This method generates a new game and assigns it to a given player */
    public Optional<Game> newGame(int id) {
        try {
            Optional<User> user = userRepositoryMySQL.findById(id);
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
    /* This method retrieves the games from a given player */
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
    /* This method gets all players alongside their scores */
    public Optional<List<PlayerRankingDTO>> getAllPlayers() {
            List<PlayerRankingDTO> playersRankingDTO = new ArrayList<>();
            List<User> players = userRepositoryMySQL.findAll();
            for(User player:players) {
                PlayerRankingDTO playerRankingDTO = fromUserToPlayerRankingDTO(player);
                playersRankingDTO.add(playerRankingDTO);
            }
        return Optional.of(playersRankingDTO);
    }
    /* This method deletes a given player */
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
    /* This method retrieves all players ordered depending on their score */
    public Optional<List<PlayerRankingDTO>> playersRanking() {
        List<PlayerRankingDTO> playersRankingDTO = userRepositoryMySQL.findAll().stream().map(this::fromUserToPlayerRankingDTO).sorted().collect(Collectors.toList());
        return Optional.of(playersRankingDTO);
    }
    /* This method calculates and retrieves the average success rate from all players */
    public OptionalDouble averageSuccess() {
        List<PlayerRankingDTO> playersRankingDTO = userRepositoryMySQL.findAll().stream().map(this::fromUserToPlayerRankingDTO).toList();
        return playersRankingDTO.stream().mapToDouble(PlayerRankingDTO::getSuccessRate).average();
    }
    /* This method retrieves the best player depending on his success rate */
    public Optional<PlayerRankingDTO> bestPlayer() {
        return getAllPlayers().get().stream().sorted().findFirst();

    }
    /* This method retrieves the worst player depending on his success rate */
    public Optional<PlayerRankingDTO> worstPlayer() {
        return getAllPlayers().get().stream().max(Comparator.naturalOrder());
    }

    /* This method transform the user entity to a player dto */
    private PlayerDTO fromUserToPlayerDTO(User player) {
        ModelMapper modelMapper = new ModelMapper();
        PlayerDTO playerDTO = modelMapper.map(player, PlayerDTO.class);
        playerDTO.setGames(new ArrayList<>());
        List<Game> games = gameRepositoryMySQL.findGamesByPlayer(player).get();
        playerDTO.setGames(games);
        playerDTO.setSuccessRate(playerDTO.successRatePlayerCalculator());
        return playerDTO;
    }
    /* This method transform the user entity to a player ranking dto */
    private PlayerRankingDTO fromUserToPlayerRankingDTO(User player) {
        ModelMapper modelMapper = new ModelMapper();
        PlayerRankingDTO playerRankingDTO = modelMapper.map(player, PlayerRankingDTO.class);
        List<Game> games = gameRepositoryMySQL.findGamesByPlayer(player).get();
        double averageScore=0;
        for(Game game: games) {
            if(game.getGameStatus().equals(GameStatus.WIN)) {
                averageScore++;
            }
        }
        averageScore=averageScore/games.size();
        playerRankingDTO.setSuccessRate(averageScore);
        return playerRankingDTO;
    }
}
