package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.auth.AuthenticationResponse;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.config.JwtService;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.GameStatus;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Role;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerRankingDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.GameRepositoryMySQL;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.UserRepositoryMongo;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.UserRepositoryMySQL;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/* This class implements the business logic. Sets how elements are updated and fetched and calculates average scores */
@Service
public final class DiceGameServices implements ServicesInterface {

    private final UserRepositoryMySQL userRepositoryMySQL;
    private final GameRepositoryMySQL gameRepositoryMySQL;
    private final UserRepositoryMongo userRepositoryMongo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public DiceGameServices(UserRepositoryMySQL userRepositoryMySQL, GameRepositoryMySQL gameRepositoryMySQL, UserRepositoryMongo userRepositoryMongo, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepositoryMySQL = userRepositoryMySQL;
        this.gameRepositoryMySQL = gameRepositoryMySQL;
        this.userRepositoryMongo = userRepositoryMongo;
        this.passwordEncoder = passwordEncoder;

        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    /* This method allows to register users, both Admin and Players.
    * We do not allow duplicated names nor emails and sets the default player name to ANONYMOUS.
    * Only one ADMIN is allowed.
    *  */
    public AuthenticationResponse register(User user) throws PlayerNotFoundException {
        boolean isNameExists = userRepositoryMySQL.getUsersByName(user.getName())
                .map(users -> users.stream().anyMatch(u -> !u.getName().equals(User.DEFAULT_PLAYER)))
                .orElse(false);

        boolean isEmailExists = userRepositoryMySQL.findByEmail(user.getEmail()).isPresent()
                || userRepositoryMongo.findByEmail(user.getEmail()).isPresent();

        if (isNameExists || isEmailExists) {
            throw new PlayerNotFoundException();
        }

        if (user.getRole() == Role.ADMIN) {
            var admin = User.builder()
                    .name(User.DEFAULT_ADMIN)
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .role(user.getRole())
                    .dateOfRegistration(LocalDateTime.now().toString())
                    .build();
            userRepositoryMongo.save(admin);
            var jwtToken = jwtService.generateToken(admin);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            var player = User.builder()
                    .name(user.getName() == null ? User.DEFAULT_PLAYER : user.getName())
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .role(user.getRole())
                    .dateOfRegistration(LocalDateTime.now().toString())
                    .build();
            userRepositoryMySQL.save(player);
            var jwtToken = jwtService.generateToken(player);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
    }

    /* This method allows to authenticate users, both Admin and Players */
    public AuthenticationResponse authenticate(User user) throws PlayerNotFoundException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if (user.getRole().equals(Role.PLAYER)) {
            var player = userRepositoryMySQL.findByEmail(user.getEmail()).orElseThrow(PlayerNotFoundException::new);
            var jwtToken = jwtService.generateToken(player);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            var admin = userRepositoryMongo.findByEmail(user.getEmail()).orElseThrow(PlayerNotFoundException::new);
            var jwtToken = jwtService.generateToken(admin);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
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
    public List<PlayerRankingDTO> retrieveAllPlayers() {
            List<PlayerRankingDTO> playersRankingDTO = new ArrayList<>();
            List<User> players = userRepositoryMySQL.findAll();
            if(players.isEmpty()) {
                return null;
            }
            for(User player:players) {
                PlayerRankingDTO playerRankingDTO = fromUserToPlayerRankingDTO(player);
                playersRankingDTO.add(playerRankingDTO);
            }
        return playersRankingDTO;
    }
    /* This method deletes a given player and his related games */
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
        if(!retrieveAllPlayers().isEmpty()) {
            return retrieveAllPlayers().stream().sorted().findFirst();
        }
        else {
            return Optional.empty();
        }

    }
    /* This method retrieves the worst player depending on his success rate */
    public Optional<PlayerRankingDTO> worstPlayer() {
        if(!retrieveAllPlayers().isEmpty()) {
            return retrieveAllPlayers().stream().max(Comparator.naturalOrder());
        }
        else {
            return Optional.empty();
        }
    }

    /* This method transform the user entity to a player dto */
    PlayerDTO fromUserToPlayerDTO(User player) {
        ModelMapper modelMapper = new ModelMapper();
        PlayerDTO playerDTO = modelMapper.map(player, PlayerDTO.class);
        playerDTO.setGames(new ArrayList<>());
        List<Game> games = gameRepositoryMySQL.findGamesByPlayer(player);
        playerDTO.setGames(games);
        playerDTO.setSuccessRate(playerDTO.successRatePlayerCalculator());
        return playerDTO;
    }
    /* This method transform the user entity to a player ranking dto */
    public PlayerRankingDTO fromUserToPlayerRankingDTO(User player) {
        ModelMapper modelMapper = new ModelMapper();
        PlayerRankingDTO playerRankingDTO = modelMapper.map(player, PlayerRankingDTO.class);
        List<Game> games = gameRepositoryMySQL.findGamesByPlayer(player);
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
