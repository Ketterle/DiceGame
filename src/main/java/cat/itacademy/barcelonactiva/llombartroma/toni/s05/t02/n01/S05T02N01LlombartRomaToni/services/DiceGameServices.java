package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.auth.AuthenticationResponse;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.auth.PlayerAlreadyExistsException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.config.JwtService;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.GameStatus;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Role;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerWithGamesDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerWithoutGamesDTO;
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
    public AuthenticationResponse register(User user) throws PlayerAlreadyExistsException {
        boolean isNameExists = userRepositoryMySQL.getUsersByName(user.getName())
                .map(users -> users.stream().anyMatch(u -> !u.getName().equals(User.DEFAULT_PLAYER)))
                .orElse(false);

        boolean isEmailExists = userRepositoryMySQL.findByEmail(user.getEmail()).isPresent()
                || userRepositoryMongo.findByEmail(user.getEmail()).isPresent();

        if (isNameExists || isEmailExists) {
            throw new PlayerAlreadyExistsException();
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
    public Optional<PlayerWithGamesDTO> update(String name, int id) {
        try {
            Optional<User> optionalPlayer = userRepositoryMySQL.findById(id);
            if(optionalPlayer.isPresent()) {
                User updateUser = optionalPlayer.get();
                updateUser.setName(name);
                return Optional.of(fromUserToPlayerWithGamesDTO(userRepositoryMySQL.save(updateUser)));
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
    public List<PlayerWithoutGamesDTO> retrieveAllPlayers() {
            List<PlayerWithoutGamesDTO> playersRankingDTO = new ArrayList<>();
            List<User> players = userRepositoryMySQL.findAll();
            if(players.isEmpty()) {
                return null;
            }
            for(User player:players) {
                PlayerWithoutGamesDTO playerWithoutGamesDTO = fromUserToPlayerWithoutGamesDTO(player);
                playersRankingDTO.add(playerWithoutGamesDTO);
            }
        return playersRankingDTO;
    }
    /* This method deletes a given player and his related games */
    public Optional<PlayerWithGamesDTO> delete(int id) {
        Optional<User> optionalPlayer = userRepositoryMySQL.findById(id);
        try {
            if (optionalPlayer.isPresent()) {
                optionalPlayer.ifPresent(player -> userRepositoryMySQL.deleteById(player.getId()));
                return Optional.of(fromUserToPlayerWithGamesDTO(optionalPlayer.get()));
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
    public Optional<List<PlayerWithoutGamesDTO>> playersRanking() {
        List<PlayerWithoutGamesDTO> playersRankingDTO = userRepositoryMySQL.findAll().stream().map(this::fromUserToPlayerWithoutGamesDTO).sorted().collect(Collectors.toList());
        return Optional.of(playersRankingDTO);
    }
    /* This method calculates and retrieves the average success rate from all players */
    public OptionalDouble averageSuccess() {
        List<PlayerWithoutGamesDTO> playersRankingDTO = userRepositoryMySQL.findAll().stream().map(this::fromUserToPlayerWithoutGamesDTO).toList();
        return playersRankingDTO.stream().mapToDouble(PlayerWithoutGamesDTO::getSuccessRate).average();
    }
    /* This method retrieves the best player depending on his success rate */
    public Optional<PlayerWithoutGamesDTO> bestPlayer() {
        if(!Objects.requireNonNull(retrieveAllPlayers()).isEmpty()) {
            return Objects.requireNonNull(retrieveAllPlayers()).stream().sorted().findFirst();
        }
        else {
            return Optional.empty();
        }

    }
    /* This method retrieves the worst player depending on his success rate */
    public Optional<PlayerWithoutGamesDTO> worstPlayer() {
        if(!Objects.requireNonNull(retrieveAllPlayers()).isEmpty()) {
            return Objects.requireNonNull(retrieveAllPlayers()).stream().max(Comparator.naturalOrder());
        }
        else {
            return Optional.empty();
        }
    }

    /* This method transform the user entity to a player dto alongside his associated games */
    PlayerWithGamesDTO fromUserToPlayerWithGamesDTO(User player) {
        ModelMapper modelMapper = new ModelMapper();
        PlayerWithGamesDTO playerWithGamesDTO = modelMapper.map(player, PlayerWithGamesDTO.class);
        playerWithGamesDTO.setGames(new ArrayList<>());
        List<Game> games = gameRepositoryMySQL.findGamesByPlayer(player);
        playerWithGamesDTO.setGames(games);
        playerWithGamesDTO.setSuccessRate(playerWithGamesDTO.successRatePlayerCalculator());
        return playerWithGamesDTO;
    }
    /* This method transform the user entity to a player dto without his associated games */
    public PlayerWithoutGamesDTO fromUserToPlayerWithoutGamesDTO(User player) {
        ModelMapper modelMapper = new ModelMapper();
        PlayerWithoutGamesDTO playerWithoutGamesDTO = modelMapper.map(player, PlayerWithoutGamesDTO.class);
        List<Game> games = gameRepositoryMySQL.findGamesByPlayer(player);
        double averageScore=0;
        for(Game game: games) {
            if(game.getGameStatus().equals(GameStatus.WIN)) {
                averageScore++;
            }
        }
        averageScore=averageScore/games.size();
        playerWithoutGamesDTO.setSuccessRate(averageScore);
        return playerWithoutGamesDTO;
    }
}
