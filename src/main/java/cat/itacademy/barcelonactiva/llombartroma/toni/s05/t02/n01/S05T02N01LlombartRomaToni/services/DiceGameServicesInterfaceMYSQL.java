package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.UserDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.GameRepositoryMySQL;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.UserRepositoryMySQL;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiceGameServicesInterfaceMYSQL implements ServicesInterface {

    private final UserRepositoryMySQL userRepositoryMySQL;
    private final GameRepositoryMySQL gameRepositoryMySQL;

    public DiceGameServicesInterfaceMYSQL(UserRepositoryMySQL userRepositoryMySQL, GameRepositoryMySQL gameRepositoryMySQL) {
        this.userRepositoryMySQL = userRepositoryMySQL;
        this.gameRepositoryMySQL = gameRepositoryMySQL;

    }

    /* Implements how API add a new User */
    public Optional<UserDTO> add(User user) {
        Optional<List<User>> playerRetrievedOptional = userRepositoryMySQL.getUsersByName(user.getName());
        if (!playerRetrievedOptional.get().isEmpty() && playerRetrievedOptional.get().stream().noneMatch(s->s.getName().equals((User.DEFAULT_NAME)))) {
            return Optional.empty();
        } else {
            return Optional.of(User.fromUserToUserDTO(userRepositoryMySQL.save(user)));
        }
    }

    public Optional<UserDTO> update(String name, int id) {
        Optional<User> optionalPlayer = userRepositoryMySQL.findById(id);
        User updateUser = optionalPlayer.get();
        updateUser.setName(name);
        return Optional.of(User.fromUserToUserDTO(userRepositoryMySQL.save(updateUser)));
    }
    public Optional<Game> newGame(int id) {
        try {
            Optional<User> user = userRepositoryMySQL.findById(id);
            if(user.isPresent()) {
                Game game = new Game();
                game.setUser(user.get());
                user.get().getGames().add(game);
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
                List<GameDTO> games = gameRepositoryMySQL.findAllByUserId(id).stream().map(Game::fromGameToGameDTO).collect(Collectors.toList());
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
    public Optional<List<UserDTO>> getAllPlayers() {
        List<UserDTO> playersDTO = userRepositoryMySQL.findAll().stream().map(User::fromUserToUserDTO).collect(Collectors.toList());
        return Optional.of(playersDTO);
    }
    public Optional<UserDTO> delete(int id) {
        Optional<User> optionalPlayer = userRepositoryMySQL.findById(id);
        try {
            if (optionalPlayer.isPresent()) {
                optionalPlayer.ifPresent(player -> userRepositoryMySQL.deleteById(player.getId()));
                return Optional.of(User.fromUserToUserDTO(optionalPlayer.get()));
            } else {
                throw new PlayerNotFoundException();
            }
        }
        catch(PlayerNotFoundException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<List<UserDTO>> playersRanking() {
        List<UserDTO> playersDTO = userRepositoryMySQL.findAll().stream().map(User::fromUserToUserDTO).sorted().collect(Collectors.toList());
        return Optional.of(playersDTO);
    }
    public OptionalDouble averageSuccess() {
        List<UserDTO> playersDTO = userRepositoryMySQL.findAll().stream().map(User::fromUserToUserDTO).toList();
        return playersDTO.stream().mapToDouble(UserDTO::getSuccessRate).average();
    }
    public Optional<UserDTO> bestPlayer() {
        return getAllPlayers().get().stream().sorted().findFirst();

    }
    public Optional<UserDTO> worstPlayer() {
        return getAllPlayers().get().stream().max(Comparator.naturalOrder());
    }

}
