package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.auth.AuthenticationResponse;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerRankingDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ServiceToControllerTest {
    private ServiceToController serviceToController;
    private DiceGameServices diceGameServicesMock;

    @BeforeEach
    void setUp() {
        diceGameServicesMock = mock(DiceGameServices.class);
        serviceToController = new ServiceToController(diceGameServicesMock);
    }

    @Test
    void testRegister() {
        // Arrange
        User user = new User(); // Create a sample user object

        AuthenticationResponse expectedResponse = new AuthenticationResponse();
        // Set the expected response properties based on your implementation

        DiceGameServices diceGameServicesMock = Mockito.mock(DiceGameServices.class);
        try {
            Mockito.when(diceGameServicesMock.register(user)).thenReturn(expectedResponse);
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }

        ServiceToController serviceToController = new ServiceToController(diceGameServicesMock);

        // Act
        AuthenticationResponse result = serviceToController.register(user);

        // Assert
        Assertions.assertEquals(expectedResponse, result);

        try {
            Mockito.verify(diceGameServicesMock).register(user);
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }
        Mockito.verifyNoMoreInteractions(diceGameServicesMock);
    }


    @Test
    void testAuthenticate() {
        // Arrange
        User user = new User(); // Create a sample user object

        AuthenticationResponse expectedResponse = new AuthenticationResponse();
        // Set the expected response properties based on your implementation

        DiceGameServices diceGameServicesMock = Mockito.mock(DiceGameServices.class);
        try {
            Mockito.when(diceGameServicesMock.authenticate(user)).thenReturn(expectedResponse);
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }

        ServiceToController serviceToController = new ServiceToController(diceGameServicesMock);

        // Act
        AuthenticationResponse result = serviceToController.authenticate(user);

        // Assert
        Assertions.assertEquals(expectedResponse, result);

        try {
            Mockito.verify(diceGameServicesMock).authenticate(user);
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }
        Mockito.verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void testUpdateSuccess() {
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
        Assertions.assertEquals(updatedPlayerDTO, result.get());

        verify(diceGameServicesMock).update(newName, playerId);
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void testNewGameSuccess() {
        // Arrange
        int playerId = 1;
        Game newGame = new Game(); // Create a sample new game

        when(diceGameServicesMock.newGame(playerId))
                .thenReturn(Optional.of(newGame));

        // Act
        Optional<Game> result = serviceToController.newGame(playerId);

        // Assert
        assertTrue(result.isPresent());
        Assertions.assertEquals(newGame, result.get());

        verify(diceGameServicesMock).newGame(playerId);
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void testGetPlayerGamesSuccess() {
        // Arrange
        int playerId = 1;
        List<GameDTO> playerGames = new ArrayList<>(); // Create a sample list of player games

        when(diceGameServicesMock.getPlayerGames(playerId))
                .thenReturn(Optional.of(playerGames));

        // Act
        Optional<List<GameDTO>> result = serviceToController.getPlayerGames(playerId);

        // Assert
        assertTrue(result.isPresent());
        Assertions.assertEquals(playerGames, result.get());

        verify(diceGameServicesMock).getPlayerGames(playerId);
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void testGetAllPlayers() {
        // Arrange
        List<PlayerRankingDTO> allPlayers = new ArrayList<>(); // Create a sample list of all players
        allPlayers.add(new PlayerRankingDTO());

        when(diceGameServicesMock.retrieveAllPlayers()).thenReturn(allPlayers);

        // Act
        List<PlayerRankingDTO> result = serviceToController.retrieveAllPlayers();

        // Assert
        assertFalse(result.isEmpty());
        Assertions.assertEquals(allPlayers, result);

        verify(diceGameServicesMock).retrieveAllPlayers();
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void testDeleteSuccess() {
        // Arrange
        int playerId = 1;
        PlayerDTO deletedPlayer = new PlayerDTO(); // Create a sample deleted player

        when(diceGameServicesMock.delete(playerId))
                .thenReturn(Optional.of(deletedPlayer));

        // Act
        Optional<PlayerDTO> result = serviceToController.delete(playerId);

        // Assert
        assertTrue(result.isPresent());
        Assertions.assertEquals(deletedPlayer, result.get());

        verify(diceGameServicesMock).delete(playerId);
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void testPlayersRanking() {
        // Arrange
        List<PlayerRankingDTO> playerRanking = new ArrayList<>(); // Create a sample player ranking

        when(diceGameServicesMock.playersRanking())
                .thenReturn(Optional.of(playerRanking));

        // Act
        Optional<List<PlayerRankingDTO>> result = serviceToController.playersRanking();

        // Assert
        assertTrue(result.isPresent());
        Assertions.assertEquals(playerRanking, result.get());

        verify(diceGameServicesMock).playersRanking();
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void testAverageSuccess() {
        // Arrange
        OptionalDouble averageSuccessRate = OptionalDouble.of(0.75); // Create a sample average success rate

        when(diceGameServicesMock.averageSuccess())
                .thenReturn(averageSuccessRate);

        // Act
        OptionalDouble result = serviceToController.averageSuccess();

        // Assert
        assertTrue(result.isPresent());
        Assertions.assertEquals(averageSuccessRate.getAsDouble(), result.getAsDouble(), 0.001);

        verify(diceGameServicesMock).averageSuccess();
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void testBestPlayer() {
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
        Assertions.assertEquals(bestPlayer, result.get());

        verify(diceGameServicesMock).bestPlayer();
        verifyNoMoreInteractions(diceGameServicesMock);
    }

    @Test
    void testWorstPlayer() {
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
        Assertions.assertEquals(worstPlayer, result.get());

        verify(diceGameServicesMock).worstPlayer();
        verifyNoMoreInteractions(diceGameServicesMock);
    }

}
