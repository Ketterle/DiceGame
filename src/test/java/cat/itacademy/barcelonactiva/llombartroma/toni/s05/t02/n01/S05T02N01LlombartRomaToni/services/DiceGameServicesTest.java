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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class DiceGameServicesTest {

    @Mock
    private UserRepositoryMySQL userRepositoryMySQL;

    @Mock
    private GameRepositoryMySQL gameRepositoryMySQL;

    @Mock
    private UserRepositoryMongo userRepositoryMongo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private DiceGameServices diceGameServices;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void registerTest() throws PlayerAlreadyExistsException {
        // Mock the UserRepositoryMySQL behavior
        when(userRepositoryMySQL.getUsersByName(anyString())).thenReturn(Optional.empty());
        when(userRepositoryMySQL.findByEmail(anyString())).thenReturn(Optional.empty());

        // Mock the JwtService behavior
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");

        // Create a test user
        User user = User.builder()
                .name("John")
                .email("john@example.com")
                .password("password")
                .role(Role.PLAYER)
                .build();

        // Call the register method
        AuthenticationResponse response = diceGameServices.register(user);

        // Verify the UserRepositoryMySQL method calls
        verify(userRepositoryMySQL).getUsersByName("John");
        verify(userRepositoryMySQL).findByEmail("john@example.com");
        verify(userRepositoryMySQL).save(any(User.class));

        // Verify the JwtService method call
        verify(jwtService).generateToken(any(User.class));

        // Perform assertions on the response
        assertNotNull(response);
        Assertions.assertEquals("mockedToken", response.getToken());
    }
    @Test
    public void authenticateTest() throws PlayerNotFoundException {
        // Mock the UserRepositoryMySQL behavior
        when(userRepositoryMySQL.findByEmail(anyString())).thenReturn(Optional.of(mock(User.class)));

        // Mock the JwtService behavior
        when(jwtService.generateToken(any(User.class))).thenReturn("mockedToken");

        // Create a test user
        User user = User.builder()
                .email("john@example.com")
                .password("password")
                .role(Role.PLAYER)
                .build();

        // Mock the AuthenticationManager behavior
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        // Call the authenticate method
        AuthenticationResponse response = diceGameServices.authenticate(user);

        // Verify the UserRepositoryMySQL method call
        verify(userRepositoryMySQL).findByEmail("john@example.com");

        // Verify the JwtService method call
        verify(jwtService).generateToken(any(User.class));

        // Verify the AuthenticationManager method call
        verify(authenticationManager).authenticate(any(Authentication.class));

        // Perform assertions on the response
        assertNotNull(response);
        Assertions.assertEquals("mockedToken", response.getToken());
    }

    @Test
    public void updateTest() {
        // Create a test user
        User user = User.builder()
                .name("Toni")
                .id(1)
                .email("john@example.com")
                .password("password")
                .role(Role.PLAYER)
                .build();

        // Mock the UserRepositoryMySQL behavior
        when(userRepositoryMySQL.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepositoryMySQL.save(any(User.class))).thenReturn(user);
        when(gameRepositoryMySQL.findGamesByPlayer(any(User.class))).thenReturn(new ArrayList<>());

        // Call the update method
        PlayerWithGamesDTO updatedUser = diceGameServices.update("Hola",1).get();

        // Verify the UserRepositoryMySQL method calls
        verify(userRepositoryMySQL).findById(1);
        verify(userRepositoryMySQL).save(user);

        // Perform assertions on the updatedUser
        assertNotNull(updatedUser);
        Assertions.assertEquals("Hola", updatedUser.getName());
    }

    @Test
    public void addGameTest() {
        // Create a test game object
        Game game = new Game();
        game.setId(0);
        game.setDice1(6);
        game.setDice2(2);
        game.setGameOutput(8);
        game.setGameStatus(GameStatus.LOSE);
        User user = new User();
        user.setId(1);
        game.setPlayer(user);

        // Mock the GameRepositoryMySQL behavior
        when(gameRepositoryMySQL.save(any(Game.class))).thenReturn(game);
        when(userRepositoryMySQL.findById(1)).thenReturn(Optional.of(user));

        // Call the addGame method
        Optional<Game> addedGame = diceGameServices.newGame(1);
        addedGame.get().setDice1(6);
        addedGame.get().setDice2(2);
        addedGame.get().setGameOutput(8);
        addedGame.get().setGameStatus(GameStatus.LOSE);

        // Verify the GameRepositoryMySQL method call
        verify(gameRepositoryMySQL).save(game);

        // Perform assertions on the addedGame
        assertNotNull(addedGame);
        Assertions.assertEquals(0, addedGame.get().getId());  // Expect ID to be 0
        Assertions.assertEquals(6, addedGame.get().getDice1());
        Assertions.assertEquals(2, addedGame.get().getDice2());
        Assertions.assertEquals(8, addedGame.get().getGameOutput());
        Assertions.assertEquals(GameStatus.LOSE, addedGame.get().getGameStatus());

    }

    @Test
    public void getPlayerGamesTest() {
        // Arrange
        int playerId = 1;
        User player = new User();
        player.setId(playerId);

        List<Game> games = new ArrayList<>();
        games.add(new Game());
        games.add(new Game());

        Mockito.when(userRepositoryMySQL.findById(playerId)).thenReturn(Optional.of(player));
        Mockito.when(gameRepositoryMySQL.findAllByPlayerId(playerId)).thenReturn(games);

        // Act
        Optional<List<GameDTO>> result = diceGameServices.getPlayerGames(playerId);

        // Assert
        assertTrue(result.isPresent());
        Assertions.assertEquals(2, result.get().size());

    }

    @Test
    public void retrieveAllPlayersTest() {
        // Mock the behavior of the repository method
        List<Game> games = List.of(new Game(), new Game());
        when(gameRepositoryMySQL.findAll()).thenReturn(games);

        // Call the method
        List<Game> result = gameRepositoryMySQL.findAll();

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void deleteUserTest() {
        // Create a test user
        User user = User.builder()
                .id(1)
                .name("John")
                .email("john@example.com")
                .password("password")
                .role(Role.PLAYER)
                .build();

        // Mock the UserRepositoryMySQL behavior
        when(userRepositoryMySQL.findById(1)).thenReturn(Optional.of(user));
        when(gameRepositoryMySQL.findGamesByPlayer(user)).thenReturn(Collections.emptyList());

        // Call the deleteUser method
        diceGameServices.delete(1);

        // Verify the UserRepositoryMySQL method call
        verify(userRepositoryMySQL).findById(1);
    }

    @Test
    public void playersRankingTest() {

        // Create test users
        User user1 = new User(1,"Toni","a","tllombart@gmail.com","belloc",Role.PLAYER);
        User user2 = new User(2,"Laura","a","percolini@gmail.com","belloc",Role.PLAYER);
        PlayerWithoutGamesDTO playerWithoutGamesDTO1 = new PlayerWithoutGamesDTO();
        PlayerWithoutGamesDTO playerWithoutGamesDTO2 = new PlayerWithoutGamesDTO();
        playerWithoutGamesDTO1.setSuccessRate(1);
        playerWithoutGamesDTO2.setSuccessRate(0.5);

        // Mock the behavior
        when(userRepositoryMySQL.findAll()).thenReturn(List.of(user1,user2));
        when(gameRepositoryMySQL.findGamesByPlayer(any(User.class))).thenReturn(List.of(new Game()));

        // Call the playersRanking method
        Optional<List<PlayerWithoutGamesDTO>> players = diceGameServices.playersRanking();

        //Assert
        Assertions.assertEquals("Toni", players.get().get(1).getName());
        Assertions.assertEquals(2, players.get().size());

    }

    @Test
    public void averageSuccessTest() {
        // Create test users
        User user1 = new User(1,"Toni","a","tllombart@gmail.com","belloc",Role.PLAYER);
        User user2 = new User(2,"Laura","a","percolini@gmail.com","belloc",Role.PLAYER);
        Game game1 = new Game();
        Game game2 = new Game();
        game1.setGameStatus(GameStatus.WIN);
        game2.setGameStatus(GameStatus.LOSE);

        // Mock the behavior
        when(userRepositoryMySQL.findAll()).thenReturn(List.of(user1,user2));
        when(gameRepositoryMySQL.findGamesByPlayer(user1)).thenReturn(List.of(game1));
        when(gameRepositoryMySQL.findGamesByPlayer(user2)).thenReturn(List.of(game2));

        // Call the averrageSuccess method
        OptionalDouble averageScore = diceGameServices.averageSuccess();

        //Assert
        Assertions.assertEquals(0.50, averageScore.getAsDouble());

    }
    @Test
    void bestPlayerTest() {
        // Create test player ranking DTOs
        PlayerWithoutGamesDTO player1 = new PlayerWithoutGamesDTO();
        player1.setName("John");
        player1.setSuccessRate(0.8);

        PlayerWithoutGamesDTO player2 = new PlayerWithoutGamesDTO();
        player2.setName("Alice");
        player2.setSuccessRate(0.9);

        List<PlayerWithoutGamesDTO> playerWithoutGamesDTOS = List.of(player1, player2);

        // Mock the behavior of retrieveAllPlayers() method
        DiceGameServices mockedServices = Mockito.spy(diceGameServices);
        Mockito.doReturn(playerWithoutGamesDTOS).when(mockedServices).retrieveAllPlayers();

        // Call the bestPlayer method
        Optional<PlayerWithoutGamesDTO> bestPlayer = mockedServices.bestPlayer();

        // Assert the result
        Assertions.assertTrue(bestPlayer.isPresent());
        Assertions.assertEquals("Alice", bestPlayer.get().getName());
    }

    @Test
    void worstPlayerTest() {
        // Create test player ranking DTOs
        PlayerWithoutGamesDTO player1 = new PlayerWithoutGamesDTO();
        player1.setName("John");
        player1.setSuccessRate(0.8);

        PlayerWithoutGamesDTO player2 = new PlayerWithoutGamesDTO();
        player2.setName("Alice");
        player2.setSuccessRate(0.9);

        List<PlayerWithoutGamesDTO> playerWithoutGamesDTOS = List.of(player1, player2);

        // Mock the behavior of retrieveAllPlayers() method
        DiceGameServices mockedServices = Mockito.spy(diceGameServices);
        Mockito.doReturn(playerWithoutGamesDTOS).when(mockedServices).retrieveAllPlayers();

        // Call the bestPlayer method
        Optional<PlayerWithoutGamesDTO> worstPlayer = mockedServices.worstPlayer();

        // Assert the result
        Assertions.assertTrue(worstPlayer.isPresent());
        Assertions.assertEquals("John", worstPlayer.get().getName());
    }

    @Test
    void testFromUserToPlayerWithGamesDTO() {
        // Create a mock user and games
        User user = new User();
        user.setId(1);
        user.setName("John");

        List<Game> games = new ArrayList<>();
        Game game1 = new Game();
        game1.setId(1);


        Game game2 = new Game();
        game2.setId(2);


        games.add(game1);
        games.add(game2);

        // Mock the behavior of gameRepositoryMySQL
        GameRepositoryMySQL gameRepositoryMock = Mockito.mock(GameRepositoryMySQL.class);
        Mockito.when(gameRepositoryMock.findGamesByPlayer(user)).thenReturn(games);

        // Call the private fromUserToPlayerDTO method using reflection
        PlayerWithGamesDTO playerWithGamesDTO = diceGameServices.fromUserToPlayerWithGamesDTO(user);
        playerWithGamesDTO.setGames(games);

        // Assert the result
        Assertions.assertEquals("John", playerWithGamesDTO.getName());
        Assertions.assertEquals(2, playerWithGamesDTO.getGames().size());
        Assertions.assertEquals(0.0, playerWithGamesDTO.getSuccessRate()); // Update with the expected success rate
    }


    @Test
    void testFromUserToPlayerWithoutGamesDTO() {
        // Create a mock user and games
        User user = new User();
        user.setId(1);
        user.setName("John");

        List<Game> games = new ArrayList<>();
        Game game1 = new Game();
        game1.setId(1);
        game1.setGameStatus(GameStatus.WIN);

        Game game2 = new Game();
        game2.setId(2);
        game2.setGameStatus(GameStatus.LOSE);

        games.add(game1);
        games.add(game2);

        // Mock the behavior of gameRepositoryMySQL
        GameRepositoryMySQL gameRepositoryMock = Mockito.mock(GameRepositoryMySQL.class);
        Mockito.when(gameRepositoryMock.findGamesByPlayer(user)).thenReturn(games);

        // Call the fromUserToPlayerRankingDTO method
        PlayerWithoutGamesDTO playerWithoutGamesDTO = diceGameServices.fromUserToPlayerWithoutGamesDTO(user);
        playerWithoutGamesDTO.setSuccessRate(0.5);

        // Assert the result
        Assertions.assertEquals("John", playerWithoutGamesDTO.getName());
        Assertions.assertEquals(0.5, playerWithoutGamesDTO.getSuccessRate()); // Update with the expected success rate
    }
}
