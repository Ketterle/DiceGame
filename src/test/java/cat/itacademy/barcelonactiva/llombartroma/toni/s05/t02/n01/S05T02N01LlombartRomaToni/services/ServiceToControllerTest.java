package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerRankingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

class ServiceToControllerTest {
    private ServiceToController serviceToController;
    private DiceGameServicesMYSQL diceGameServicesMock;

    @BeforeEach
    void setUp() {
        diceGameServicesMock = mock(DiceGameServicesMYSQL.class);
        serviceToController = new ServiceToController(diceGameServicesMock);
    }

    @Test
    void updateSuccessTest() {
        // Arrange
        int playerId = 1;
        String newName = "New Name";
        PlayerDTO updatedPlayerDTO = new PlayerDTO(); // Create a sample updated player DTO

        when(diceGameServicesMock.update(newName, playerId))
                .thenReturn(Optional.of(updatedPlayerDTO));

        // Act
        Optional<PlayerDTO> result = serviceToController.update(newName, playerId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(updatedPlayerDTO, result.get());

        verify(diceGameServicesMock).update(newName, playerId);
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void newGameSuccessTest() {
        // Arrange
        int playerId = 1;
        Game newGame = new Game(); // Create a sample new game

        when(diceGameServicesMock.newGame(playerId))
                .thenReturn(Optional.of(newGame));

        // Act
        Optional<Game> result = serviceToController.newGame(playerId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(newGame, result.get());

        verify(diceGameServicesMock).newGame(playerId);
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void getPlayerGamesSuccessTest() {
        // Arrange
        int playerId = 1;
        List<GameDTO> playerGames = new ArrayList<>(); // Create a sample list of player games

        when(diceGameServicesMock.getPlayerGames(playerId))
                .thenReturn(Optional.of(playerGames));

        // Act
        Optional<List<GameDTO>> result = serviceToController.getPlayerGames(playerId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(playerGames, result.get());

        verify(diceGameServicesMock).getPlayerGames(playerId);
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void getAllPlayersTest() {
        // Arrange
        List<PlayerRankingDTO> allPlayers = new ArrayList<>(); // Create a sample list of all players

        when(diceGameServicesMock.getAllPlayers())
                .thenReturn(Optional.of(allPlayers));

        // Act
        Optional<List<PlayerRankingDTO>> result = serviceToController.getAllPlayers();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(allPlayers, result.get());

        verify(diceGameServicesMock).getAllPlayers();
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void deleteSuccessTest() {
        // Arrange
        int playerId = 1;
        PlayerDTO deletedPlayer = new PlayerDTO(); // Create a sample deleted player

        when(diceGameServicesMock.delete(playerId))
                .thenReturn(Optional.of(deletedPlayer));

        // Act
        Optional<PlayerDTO> result = serviceToController.delete(playerId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(deletedPlayer, result.get());

        verify(diceGameServicesMock).delete(playerId);
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void playersRankingTest() {
        // Arrange
        List<PlayerRankingDTO> playerRanking = new ArrayList<>(); // Create a sample player ranking

        when(diceGameServicesMock.playersRanking())
                .thenReturn(Optional.of(playerRanking));

        // Act
        Optional<List<PlayerRankingDTO>> result = serviceToController.playersRanking();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(playerRanking, result.get());

        verify(diceGameServicesMock).playersRanking();
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void averageSuccessTest() {
        // Arrange
        OptionalDouble averageSuccessRate = OptionalDouble.of(0.75); // Create a sample average success rate

        when(diceGameServicesMock.averageSuccess())
                .thenReturn(averageSuccessRate);

        // Act
        OptionalDouble result = serviceToController.averageSuccess();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(averageSuccessRate.getAsDouble(), result.getAsDouble(),0.001);

        verify(diceGameServicesMock).averageSuccess();
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void bestPlayerTest() {
        // Arrange
        List<PlayerRankingDTO> playersRanking = new ArrayList<>(); // Create a sample player ranking
        PlayerRankingDTO bestPlayer = new PlayerRankingDTO(); // Create a sample best player

        playersRanking.add(new PlayerRankingDTO()); // Add some players to the ranking list
        playersRanking.add(new PlayerRankingDTO());
        playersRanking.add(bestPlayer);
        playersRanking.add(new PlayerRankingDTO());

        when(diceGameServicesMock.bestPlayer())
                .thenReturn(Optional.of(bestPlayer));

        // Act
        Optional<PlayerRankingDTO> result = serviceToController.bestPlayer();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(bestPlayer, result.get());

        verify(diceGameServicesMock).bestPlayer();
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void worstPlayerTest() {
        // Arrange
        List<PlayerRankingDTO> playersRanking = new ArrayList<>(); // Create a sample player ranking
        PlayerRankingDTO worstPlayer = new PlayerRankingDTO(); // Create a sample worst player

        playersRanking.add(new PlayerRankingDTO()); // Add some players to the ranking list
        playersRanking.add(worstPlayer);
        playersRanking.add(new PlayerRankingDTO());
        playersRanking.add(new PlayerRankingDTO());

        when(diceGameServicesMock.worstPlayer())
                .thenReturn(Optional.of(worstPlayer));

        // Act
        Optional<PlayerRankingDTO> result = serviceToController.worstPlayer();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(worstPlayer, result.get());

        verify(diceGameServicesMock).worstPlayer();
        verifyNoMoreInteractions(diceGameServicesMock);
    }

}
