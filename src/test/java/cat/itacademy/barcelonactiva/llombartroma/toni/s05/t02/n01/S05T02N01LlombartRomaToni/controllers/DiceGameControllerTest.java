package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.UserDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services.ServiceToController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DiceGameControllerTest {

    private DiceGameController diceGameController;

    @Mock
    private ServiceToController serviceToController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        diceGameController = new DiceGameController(serviceToController);
    }

    @Test
    void testAddPlayer() {
        // Prepare test data
        User user = new User();
        user.setName("John");

        UserDTO userDTO = new UserDTO();
        userDTO.setName("John");

        when(serviceToController.add(user)).thenReturn(Optional.of(userDTO));

        // Call the endpoint
        ResponseEntity<UserDTO> responseEntity = diceGameController.add(user);

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isEqualTo(userDTO);

        // Verify the mock interaction
        verify(serviceToController).add(user);
    }

    @Test
    void testAddPlayer_NoContent() {
        // Prepare test data
        User user = new User();
        user.setName("John");

        when(serviceToController.add(user)).thenReturn(Optional.empty());

        // Call the endpoint
        ResponseEntity<UserDTO> responseEntity = diceGameController.add(user);

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify the mock interaction
        verify(serviceToController).add(user);
    }

    @Test
    void testUpdatePlayer() {
        // Prepare test data
        String playerName = "John";
        String playerId = "1";

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setName(playerName);

        when(serviceToController.update(playerName, playerId)).thenReturn(Optional.of(updatedUserDTO));

        // Call the endpoint
        ResponseEntity<UserDTO> responseEntity = diceGameController.updatePlayer(playerName, playerId);

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(updatedUserDTO);

        // Verify the mock interaction
        verify(serviceToController).update(playerName, playerId);
    }

    @Test
    void testUpdatePlayer_PlayerNotFound() {
        // Prepare test data
        String playerName = "John";
        String playerId = "1";

        when(serviceToController.update(playerName, playerId)).thenReturn(Optional.empty());

        // Call the endpoint
        ResponseEntity<UserDTO> responseEntity = diceGameController.updatePlayer(playerName, playerId);

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify the mock interaction
        verify(serviceToController).update(playerName, playerId);
    }


    @Test
    void testGetPlayerGames() {
        // Prepare test data
        String playerId = "1";

        List<GameDTO> games = new ArrayList<>();
        games.add(new GameDTO());
        games.add(new GameDTO());

        when(serviceToController.getPlayerGames(playerId)).thenReturn(Optional.of(games));

        // Call the endpoint
        ResponseEntity<List<GameDTO>> responseEntity = diceGameController.getPlayerGames(playerId);

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(games);

        // Verify the mock interaction
        verify(serviceToController).getPlayerGames(playerId);
    }

    @Test
    void testGetPlayerGames_NoContent() {
        // Prepare test data
        String playerId = "1";

        when(serviceToController.getPlayerGames(playerId)).thenReturn(Optional.empty());

        // Call the endpoint
        ResponseEntity<List<GameDTO>> responseEntity = diceGameController.getPlayerGames(playerId);

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify the mock interaction
        verify(serviceToController).getPlayerGames(playerId);
    }

    @Test
    void testGetAllPlayers() {
        // Prepare test data
        List<UserDTO> players = new ArrayList<>();
        UserDTO player1 = new UserDTO();
        player1.setName("John");
        UserDTO player2 = new UserDTO();
        player2.setName("Anna");
        players.add(player1);
        players.add(player2);

        when(serviceToController.getAllPlayers()).thenReturn(Optional.of(players));

        // Call the endpoint
        ResponseEntity<List<UserDTO>> responseEntity = diceGameController.getAllPlayers();

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(players);

        // Verify the mock interaction
        verify(serviceToController).getAllPlayers();
    }

    @Test
    void testGetAllPlayers_NoContent() {
        when(serviceToController.getAllPlayers()).thenReturn(Optional.empty());

        // Call the endpoint
        ResponseEntity<List<UserDTO>> responseEntity = diceGameController.getAllPlayers();

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify the mock interaction
        verify(serviceToController).getAllPlayers();
    }
    // ...

    @Test
    void testDeletePlayer() {
        // Prepare test data
        String playerId = "1";

        UserDTO player = new UserDTO();
        player.setName("John");

        when(serviceToController.delete(playerId)).thenReturn(Optional.of(player));

        // Call the endpoint
        ResponseEntity<UserDTO> responseEntity = diceGameController.delete(playerId);

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(player);

        // Verify the mock interaction
        verify(serviceToController).delete(playerId);
    }

    @Test
    void testDeletePlayer_NoContent() {
        // Prepare test data
        String playerId = "1";

        when(serviceToController.delete(playerId)).thenReturn(Optional.empty());

        // Call the endpoint
        ResponseEntity<UserDTO> responseEntity = diceGameController.delete(playerId);

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify the mock interaction
        verify(serviceToController).delete(playerId);
    }

    @Test
    void testGetPlayersRanking() {
        // Prepare test data
        List<UserDTO> players = new ArrayList<>();
        UserDTO player1 = new UserDTO();
        player1.setName("John");
        UserDTO player2 = new UserDTO();
        player1.setName("Jane");
        players.add(player1);
        players.add(player2);

        when(serviceToController.playersRanking()).thenReturn(Optional.of(players));

        // Call the endpoint
        ResponseEntity<List<UserDTO>> responseEntity = diceGameController.getPlayersRanking();

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(players);

        // Verify the mock interaction
        verify(serviceToController).playersRanking();
    }

    @Test
    void testGetPlayersRanking_NoContent() {
        when(serviceToController.playersRanking()).thenReturn(Optional.empty());

        // Call the endpoint
        ResponseEntity<List<UserDTO>> responseEntity = diceGameController.getPlayersRanking();

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify the mock interaction
        verify(serviceToController).playersRanking();
    }

    @Test
    void testGetAverageSuccess() {
        // Prepare test data
        double averageSuccess = 0.75;

        when(serviceToController.averageSuccess()).thenReturn(OptionalDouble.of(averageSuccess));

        // Call the endpoint
        ResponseEntity<Double> responseEntity = diceGameController.getAverageSuccess();

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(averageSuccess);

        // Verify the mock interaction
        verify(serviceToController).averageSuccess();
    }

    @Test
    void testGetAverageSuccess_NoContent() {
        when(serviceToController.averageSuccess()).thenReturn(OptionalDouble.empty());

        // Call the endpoint
        ResponseEntity<Double> responseEntity = diceGameController.getAverageSuccess();

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify the mock interaction
        verify(serviceToController).averageSuccess();
    }

    @Test
    void testGetBestPlayer() {
        // Prepare test data
        UserDTO bestPlayer = new UserDTO();
        bestPlayer.setName("John");

        when(serviceToController.bestPlayer()).thenReturn(Optional.of(bestPlayer));

        // Call the endpoint
        ResponseEntity<UserDTO> responseEntity = diceGameController.getBestPlayer();

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(bestPlayer);

        // Verify the mock interaction
        verify(serviceToController).bestPlayer();
    }

    @Test
    void testGetBestPlayer_NoContent() {
        when(serviceToController.bestPlayer()).thenReturn(Optional.empty());

        // Call the endpoint
        ResponseEntity<UserDTO> responseEntity = diceGameController.getBestPlayer();

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify the mock interaction
        verify(serviceToController).bestPlayer();
    }

    @Test
    void testGetWorstPlayer() {
        // Prepare test data
        UserDTO worstPlayer = new UserDTO();
        worstPlayer.setName("John");

        when(serviceToController.worstPlayer()).thenReturn(Optional.of(worstPlayer));

        // Call the endpoint
        ResponseEntity<UserDTO> responseEntity = diceGameController.getWorstPlayer();

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(worstPlayer);

        // Verify the mock interaction
        verify(serviceToController).worstPlayer();
    }

    @Test
    void testGetWorstPlayer_NoContent() {
        when(serviceToController.worstPlayer()).thenReturn(Optional.empty());

        // Call the endpoint
        ResponseEntity<UserDTO> responseEntity = diceGameController.getWorstPlayer();

        // Verify the response
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Verify the mock interaction
        verify(serviceToController).worstPlayer();
    }

}
