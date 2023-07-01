package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerRankingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServicesInterfaceTest {

    @Mock
    private ServicesInterface servicesInterface;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void updateTest() {
        // Define test data
        String name = "John";
        int id = 1;

        // Define the expected result
        PlayerDTO expectedResult = new PlayerDTO();

        // Define the behavior of the mock object
        when(servicesInterface.update(name, id)).thenReturn(Optional.of(expectedResult));

        // Call the method being tested
        Optional<PlayerDTO> actualResult = servicesInterface.update(name, id);

        // Verify the expected behavior
        assertTrue(actualResult.isPresent());
        assertEquals(expectedResult, actualResult.get());

        // Verify that the method was called with the correct arguments
        verify(servicesInterface).update(name, id);
    }

    @Test
    public void newGameTest() {
        // Define test data
        int id = 1;

        // Define the expected result
        Game expectedGame = new Game();

        // Define the behavior of the mock object
        when(servicesInterface.newGame(id)).thenReturn(Optional.of(expectedGame));

        // Call the method being tested
        Optional<Game> actualGame = servicesInterface.newGame(id);

        // Verify the expected behavior
        assertTrue(actualGame.isPresent());
        assertEquals(expectedGame, actualGame.get());

        // Verify that the method was called with the correct argument
        verify(servicesInterface).newGame(id);
    }

    @Test
    public void getPlayerGamesTest() {
        // Define test data
        int id = 1;

        // Define the expected result
        List<GameDTO> expectedGames = new ArrayList<>();

        // Define the behavior of the mock object
        when(servicesInterface.getPlayerGames(id)).thenReturn(Optional.of(expectedGames));

        // Call the method being tested
        Optional<List<GameDTO>> actualGames = servicesInterface.getPlayerGames(id);

        // Verify the expected behavior
        assertTrue(actualGames.isPresent());
        assertEquals(expectedGames, actualGames.get());

        // Verify that the method was called with the correct argument
        verify(servicesInterface).getPlayerGames(id);
    }

    @Test
    public void retrieveAllPlayersTest() {
        // Define expected result
        List<PlayerRankingDTO> expectedPlayers = new ArrayList<>();

        // Define the behavior of the mock object
        when(servicesInterface.retrieveAllPlayers()).thenReturn(expectedPlayers);

        // Call the method being tested
        List<PlayerRankingDTO> actualPlayers = servicesInterface.retrieveAllPlayers();

        // Verify the expected behavior
        assertNotNull(actualPlayers);
        assertEquals(expectedPlayers, actualPlayers);

        // Verify that the method was called
        verify(servicesInterface).retrieveAllPlayers();
    }

    @Test
    public void testPlayersRanking() {
        // Define expected result
        List<PlayerRankingDTO> expectedRanking = new ArrayList<>();

        // Define the behavior of the mock object
        when(servicesInterface.playersRanking()).thenReturn(Optional.of(expectedRanking));

        // Call the method being tested
        Optional<List<PlayerRankingDTO>> actualRanking = servicesInterface.playersRanking();

        // Verify the expected behavior
        assertTrue(actualRanking.isPresent());
        assertEquals(expectedRanking, actualRanking.get());

        // Verify that the method was called
        verify(servicesInterface).playersRanking();
    }
    @Test
    public void bestPlayerTest() {
        // Define expected result
        PlayerRankingDTO expectedBestPlayer = new PlayerRankingDTO();

        // Define the behavior of the mock object
        when(servicesInterface.bestPlayer()).thenReturn(Optional.of(expectedBestPlayer));

        // Call the method being tested
        Optional<PlayerRankingDTO> actualBestPlayer = servicesInterface.bestPlayer();

        // Verify the expected behavior
        assertTrue(actualBestPlayer.isPresent());
        assertEquals(expectedBestPlayer, actualBestPlayer.get());

        // Verify that the method was called
        verify(servicesInterface).bestPlayer();
    }


    @Test
    public void worstPlayerTest() {
        // Define expected result
        PlayerRankingDTO expectedWorstPlayer = new PlayerRankingDTO();

        // Define the behavior of the mock object
        when(servicesInterface.worstPlayer()).thenReturn(Optional.of(expectedWorstPlayer));

        // Call the method being tested
        Optional<PlayerRankingDTO> actualWorstPlayer = servicesInterface.worstPlayer();

        // Verify the expected behavior
        assertTrue(actualWorstPlayer.isPresent());
        assertEquals(expectedWorstPlayer, actualWorstPlayer.get());

        // Verify that the method was called
        verify(servicesInterface).worstPlayer();
    }


    @Test
    public void testAverageSuccess() {
        // Define expected result
        OptionalDouble expectedAverage = OptionalDouble.of(0.75);

        // Define the behavior of the mock object
        when(servicesInterface.averageSuccess()).thenReturn(expectedAverage);

        // Call the method being tested
        OptionalDouble actualAverage = servicesInterface.averageSuccess();

        // Verify the expected behavior
        assertTrue(actualAverage.isPresent());
        assertEquals(expectedAverage.getAsDouble(), actualAverage.getAsDouble(), 0.01);

        // Verify that the method was called
        verify(servicesInterface).averageSuccess();
    }
}
