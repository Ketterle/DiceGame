package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.auth.AuthenticationResponse;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerRankingDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services.ServiceToController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class DiceGameControllerTest {

    @Mock
    private ServiceToController serviceToController;

    @InjectMocks
    private DiceGameController diceGameController;

    @Before
    public void setup() {
        // Initialize mocks or stub any necessary behavior
    }

    @Test
    public void registerTest() {
        // Arrange
        User user = new User(); // Create a sample user object
        AuthenticationResponse expectedResponse = new AuthenticationResponse();
        // Set the expected response properties based on your implementation

        when(serviceToController.register(user)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AuthenticationResponse> response = diceGameController.register(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void authenticateTest() {
        // Arrange
        User user = new User(); // Create a sample user object
        AuthenticationResponse expectedResponse = new AuthenticationResponse();
        // Set the expected response properties based on your implementation

        when(serviceToController.authenticate(user)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<AuthenticationResponse> response = diceGameController.authenticate(user);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void updateSuccessTest() {
        // Arrange
        String name = "John";
        int id = 1;
        PlayerDTO playerDTO = new PlayerDTO();
        when(serviceToController.update(name, id)).thenReturn(Optional.of(playerDTO));

        // Act
        ResponseEntity<PlayerDTO> response = diceGameController.updatePlayer(name, id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(playerDTO, response.getBody());
    }

    @Test
    public void updateNotPlayerFoundTest() {
        // Arrange
        String name = "John";
        int id = 1;
        when(serviceToController.update(name, id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<PlayerDTO> response = diceGameController.updatePlayer(name, id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void addGameTest() {
        // Mocking the service response
        Game game = new Game();
        game.setId(1);
        Optional<Game> optionalGame = Optional.of(game);
        when(serviceToController.newGame(1)).thenReturn(optionalGame);

        // Testing the controller method
        ResponseEntity<Game> response = diceGameController.addGame(1);

        // Assertion
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(game, response.getBody());
    }

    @Test
    public void getPlayerGamesTest() {
        // Mocking the service response
        List<GameDTO> gameDTOList = new ArrayList<>();
        // Add some game DTOs to the list
        Optional<List<GameDTO>> optionalGameDTOList = Optional.of(gameDTOList);
        when(serviceToController.getPlayerGames(1)).thenReturn(optionalGameDTOList);

        // Testing the controller method
        ResponseEntity<List<GameDTO>> response = diceGameController.getPlayerGames(1);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gameDTOList, response.getBody());
    }

    @Test
    public void getAllPlayersTest() {
        // Mocking the service response
        List<PlayerRankingDTO> playerDTOList = new ArrayList<>();
        when(serviceToController.retrieveAllPlayers()).thenReturn(playerDTOList);

        // Testing the controller method
        ResponseEntity<List<PlayerRankingDTO>> response = diceGameController.getAllPlayers();

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(playerDTOList, response.getBody());
    }

    @Test
    public void deletePlayerTest() {
        // Mocking the service response
        int id = 1;
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setName("John");
        when(serviceToController.delete(id)).thenReturn(Optional.of(playerDTO));

        // Testing the controller method
        ResponseEntity<PlayerDTO> response = diceGameController.delete(id);

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(playerDTO.getName(),response.getBody().getName());
        verify(serviceToController, times(1)).delete(id);
    }

    @Test
    public void playersRankingTest() {
        // Mocking the service response
        List<PlayerRankingDTO> players = Arrays.asList(
                new PlayerRankingDTO(),
                new PlayerRankingDTO(),
                new PlayerRankingDTO()
        );
        when(serviceToController.playersRanking()).thenReturn(Optional.of(players));

        // Testing the controller method
        ResponseEntity<List<PlayerRankingDTO>> response = diceGameController.getPlayersRanking();

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(players, response.getBody());
    }

    @Test
    public void averageSuccessTest() {
        // Mocking the service response
        double average = 85.5;
        when(serviceToController.averageSuccess()).thenReturn(OptionalDouble.of(average));

        // Testing the controller method
        ResponseEntity<Double> response = diceGameController.getAverageSuccess();

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(average, response.getBody(), 0.01);
    }

    @Test
    public void bestPlayerTest() {
        // Mocking the service response
        PlayerRankingDTO bestPlayer = new PlayerRankingDTO();
        bestPlayer.setName("John");
        when(serviceToController.bestPlayer()).thenReturn(Optional.of(bestPlayer));

        // Testing the controller method
        ResponseEntity<PlayerRankingDTO> response = diceGameController.getBestPlayer();

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bestPlayer.getName(), response.getBody().getName());
    }

    @Test
    public void worstPlayerTest() {
        // Mocking the service response
        PlayerRankingDTO worstPlayer = new PlayerRankingDTO();
        worstPlayer.setName("John");
        when(serviceToController.worstPlayer()).thenReturn(Optional.of(worstPlayer));

        // Testing the controller method
        ResponseEntity<PlayerRankingDTO> response = diceGameController.getWorstPlayer();

        // Assertion
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(worstPlayer.getName(), response.getBody().getName());
    }

}
