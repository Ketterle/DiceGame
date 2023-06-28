package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.GameStatus;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerRankingDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.GameRepositoryMySQL;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.UserRepositoryMySQL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DiceGameServicesMYSQLTest {

    @Mock
    private UserRepositoryMySQL userRepositoryMock;

    @Mock
    private GameRepositoryMySQL gameRepositoryMock;

    @InjectMocks
    private DiceGameServicesMYSQL diceGameServices;

    private User testUser;
    private Game testGame;

    @Before
    public void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test User");

        testGame = new Game();
        testGame.setId(1);
        testGame.setPlayer(testUser);
        testGame.setGameStatus(GameStatus.WIN);
    }

    @Test
    public void updatePlayerNameTest() {
        // Arrange
        String newName = "New Name";
        when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepositoryMock.save(any(User.class))).thenReturn(testUser);
        when(gameRepositoryMock.findGamesByPlayer(testUser)).thenReturn(Optional.of(new ArrayList<>()));

        // Act
        Optional<PlayerDTO> updatedPlayer = diceGameServices.update(newName, testUser.getId());

        // Assert
        assertTrue(updatedPlayer.isPresent());
        assertEquals(newName, updatedPlayer.get().getName());
        verify(userRepositoryMock, times(1)).findById(testUser.getId());
        verify(userRepositoryMock, times(1)).save(any(User.class));
    }

    @Test
    public void updatePlayerNotFoundTest() {
        // Arrange
        String newName = "New Name";
        when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.empty());


        // Act
        Optional<PlayerDTO> updatedPlayer = diceGameServices.update(newName, testUser.getId());

        // Assert
        assertFalse(updatedPlayer.isPresent());
        verify(userRepositoryMock, times(1)).findById(testUser.getId());
        verify(userRepositoryMock, never()).save(any(User.class));
    }

    @Test
    public void newGameSuccessTest() {
        // Arrange
        when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(gameRepositoryMock.save(any(Game.class))).thenReturn(testGame);


        // Act
        Optional<Game> newGame = diceGameServices.newGame(testUser.getId());

        // Assert
        assertTrue(newGame.isPresent());
        assertEquals(testUser, newGame.get().getPlayer());
        verify(userRepositoryMock, times(1)).findById(testUser.getId());
        verify(gameRepositoryMock, times(1)).save(any(Game.class));
    }

    @Test
    public void newGamePlayerNotFoundTest() {
        // Arrange
        when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.empty());

        // Act
        Optional<Game> newGame = diceGameServices.newGame(testUser.getId());

        // Assert
        assertFalse(newGame.isPresent());
        verify(userRepositoryMock, times(1)).findById(testUser.getId());
        verify(gameRepositoryMock, never()).save(any(Game.class));
    }

    @Test
    public void getPlayerGamesSuccessTest() {
        // Arrange
        when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(gameRepositoryMock.findAllByPlayerId(testUser.getId())).thenReturn(Collections.singletonList(testGame));

        // Act
        Optional<List<GameDTO>> playerGames = diceGameServices.getPlayerGames(testUser.getId());

        // Assert
        assertTrue(playerGames.isPresent());
        assertEquals(1, playerGames.get().size());
        verify(userRepositoryMock, times(1)).findById(testUser.getId());
        verify(gameRepositoryMock, times(1)).findAllByPlayerId(testUser.getId());
    }

    @Test
    public void getPlayerGamesNotPlayerFoundTest() {
        // Arrange
        when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.empty());

        // Act
        Optional<List<GameDTO>> playerGames = diceGameServices.getPlayerGames(testUser.getId());

        // Assert
        assertFalse(playerGames.isPresent());
        verify(userRepositoryMock, times(1)).findById(testUser.getId());
        verify(gameRepositoryMock, never()).findAllByPlayerId(anyInt());
    }

    @Test
    public void getAllPlayersSuccessTest() {
        // Arrange
        List<User> players = Collections.singletonList(testUser);
        when(userRepositoryMock.findAll()).thenReturn(players);
        when(gameRepositoryMock.findGamesByPlayer(testUser)).thenReturn(Optional.of(new ArrayList<>()));

        // Act
        Optional<List<PlayerRankingDTO>> allPlayers = diceGameServices.getAllPlayers();

        // Assert
        assertTrue(allPlayers.isPresent());
        assertEquals(players.size(), allPlayers.get().size());
        verify(userRepositoryMock, times(1)).findAll();
    }

    @Test
    public void deletePlayerSuccessTest() {
        // Arrange
        when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(gameRepositoryMock.findGamesByPlayer(testUser)).thenReturn(Optional.of(new ArrayList<>()));

        // Act
        Optional<PlayerDTO> deletedPlayer = diceGameServices.delete(testUser.getId());

        // Assert
        assertTrue(deletedPlayer.isPresent());
        verify(userRepositoryMock, times(1)).findById(testUser.getId());
        verify(userRepositoryMock, times(1)).deleteById(testUser.getId());
    }

    @Test
    public void deletePlayerNotFoundTest() {
        // Arrange
        when(userRepositoryMock.findById(testUser.getId())).thenReturn(Optional.empty());

        // Act
        Optional<PlayerDTO> deletedPlayer = diceGameServices.delete(testUser.getId());

        // Assert
        assertFalse(deletedPlayer.isPresent());
        verify(userRepositoryMock, times(1)).findById(testUser.getId());
        verify(userRepositoryMock, never()).deleteById(anyInt());
    }

    @Test
    public void playersRankingTest() {
        // Arrange
        List<User> players = new ArrayList<>();
        User player1 = new User();
        player1.setId(1);
        player1.setName("Player 1");
        players.add(player1);

        User player2 = new User();
        player2.setId(2);
        player2.setName("Player 2");
        players.add(player2);

        when(userRepositoryMock.findAll()).thenReturn(players);
        when(gameRepositoryMock.findGamesByPlayer(any(User.class))).thenReturn(Optional.of(new ArrayList<>()));

        // Act
        Optional<List<PlayerRankingDTO>> playersRanking = diceGameServices.playersRanking();

        // Assert
        assertTrue(playersRanking.isPresent());
        assertEquals(players.size(), playersRanking.get().size());
        verify(userRepositoryMock, times(1)).findAll();
        verify(gameRepositoryMock, times(players.size())).findGamesByPlayer(any(User.class));
    }

    @Test
    public void averageSuccessSuccessTest() {
        // Arrange
        List<User> players = new ArrayList<>();

        List<Game> games1 = new ArrayList<>();
        User player1 = new User();
        Game game1 = new Game();
        game1.setPlayer(player1);
        game1.setGameStatus(GameStatus.WIN);
        games1.add(game1);
        player1.setId(1);
        player1.setName("Player 1");
        players.add(player1);

        List<Game> games2 = new ArrayList<>();
        User player2 = new User();
        Game game2 = new Game();
        game2.setPlayer(player2);
        game2.setGameStatus(GameStatus.LOSE);
        games2.add(game2);
        player2.setId(2);
        player2.setName("Player 2");
        players.add(player2);

        when(userRepositoryMock.findAll()).thenReturn(players);
        when(gameRepositoryMock.findGamesByPlayer(player1)).thenReturn(Optional.of(games1));
        when(gameRepositoryMock.findGamesByPlayer(player2)).thenReturn(Optional.of(games2));

        // Act
        OptionalDouble averageSuccess = diceGameServices.averageSuccess();

        // Assert
        assertTrue(averageSuccess.isPresent());
        assertEquals(0.5, averageSuccess.getAsDouble(), 0.001);
        verify(userRepositoryMock, times(1)).findAll();
        verify(gameRepositoryMock, times(players.size())).findGamesByPlayer(any(User.class));
    }

    @Test
    public void bestPlayerSuccessTest() {
        // Arrange
        List<User> players = new ArrayList<>();
        User player1 = new User();
        player1.setId(1);
        player1.setName("Player 1");
        players.add(player1);

        User player2 = new User();
        player2.setId(2);
        player2.setName("Player 2");
        players.add(player2);

        when(userRepositoryMock.findAll()).thenReturn(players);
        when(gameRepositoryMock.findGamesByPlayer(any(User.class))).thenReturn(Optional.of(new ArrayList<>()));

        // Act
        Optional<PlayerRankingDTO> bestPlayer = diceGameServices.bestPlayer();

        // Assert
        assertTrue(bestPlayer.isPresent());
        assertEquals(player1.getName(), bestPlayer.get().getName());
        verify(userRepositoryMock, times(1)).findAll();
        verify(gameRepositoryMock, times(players.size())).findGamesByPlayer(any(User.class));
    }

    @Test
    public void worstPlayerSuccessTest() {
        // Arrange
        List<User> players = new ArrayList<>();
        User player1 = new User();
        player1.setId(1);
        player1.setName("Player 1");
        players.add(player1);

        User player2 = new User();
        player2.setId(2);
        player2.setName("Player 2");
        players.add(player2);

        when(userRepositoryMock.findAll()).thenReturn(players);
        when(gameRepositoryMock.findGamesByPlayer(any(User.class))).thenReturn(Optional.of(new ArrayList<>()));

        // Act
        Optional<PlayerRankingDTO> worstPlayer = diceGameServices.worstPlayer();

        // Assert
        assertTrue(worstPlayer.isPresent());
        assertEquals(player2.getName(), worstPlayer.get().getName());
        verify(userRepositoryMock, times(1)).findAll();
        verify(gameRepositoryMock, times(players.size())).findGamesByPlayer(any(User.class));
    }

}
