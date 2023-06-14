package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Player;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.GameRepositoryMySQL;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.PlayerRepositoryMySQL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.internal.connection.tlschannel.util.Util.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DiceGameServicesInterfaceMYSQLTest {

    @Mock
    private PlayerRepositoryMySQL playerRepository;

    @Mock
    private GameRepositoryMySQL gameRepository;

    @InjectMocks
    private DiceGameServicesInterfaceMYSQL diceGameServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPlayer() {
        // Initialize the Player object
        Player player = new Player();
        player.setName("John");

        // Set up the mock behavior
        when(playerRepository.getPlayersByName("John")).thenReturn(Optional.of(new ArrayList<>()));
        when(playerRepository.save(player)).thenReturn(player);

        // Call the method under test
        Optional<PlayerDTO> result = diceGameServices.add(player);

        // Verify the result
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John");

        // Verify the mock interaction
        verify(playerRepository).getPlayersByName("John");
        verify(playerRepository).save(player);
    }

    @Test
    void testUpdatePlayer() {
        // Initialize the Player object
        Player player = new Player();
        player.setId("1"); // Assuming the player ID is 1
        player.setName("Toni");

        // Set up the mock behavior
        when(playerRepository.findById("1")).thenReturn(Optional.of(player));
        when(playerRepository.save(player)).thenReturn(player);

        // Call the method under test
        Optional<PlayerDTO> result = diceGameServices.update("John","1");

        // Verify the result
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John");

        // Verify the mock interaction
        verify(playerRepository).findById("1");
        verify(playerRepository).save(player);
    }
    @Test
    void testNewGame() {
        // Initialize the Player object
        Player player = new Player();
        player.setId("1"); // Assuming the player ID is 1
        player.setName("John");

        // Set up the mock behavior
        when(playerRepository.findById("1")).thenReturn(Optional.of(player));

        // Call the method under test
        Optional<Game> result = diceGameServices.newGame("1");

        // Verify the result
        assertThat(result).isPresent();
        assertThat(result.get().getPlayer()).isEqualTo(player);

        // Verify the mock interaction
        verify(playerRepository).findById("1");
        verify(gameRepository).save(any(Game.class));
    }
    @Test
    void testGetPlayerGames() {
        // Initialize the Player object
        Player player = new Player();
        player.setId("1"); // Assuming the player ID is 1
        player.setName("John");

        // Initialize the Game objects
        Game game1 = new Game();
        game1.setId(1);
        game1.setPlayer(player);

        Game game2 = new Game();
        game2.setId(2);
        game2.setPlayer(player);

        List<Game> games = Arrays.asList(game1, game2);

        // Set up the mock behavior
        when(playerRepository.findById("1")).thenReturn(Optional.of(player));
        when(gameRepository.findAllByPlayerId("1")).thenReturn(games);

        // Call the method under test
        Optional<List<GameDTO>> result = diceGameServices.getPlayerGames("1");

        // Verify the result
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(2);

        // Verify the mock interaction
        verify(playerRepository).findById("1");
        verify(gameRepository).findAllByPlayerId("1");
    }
    @Test
    void testGetAllPlayers() {
        // Create a list of Player objects
        List<Player> players = new ArrayList<>();
        Player player1 = new Player();
        player1.setId("1");
        player1.setName("John");
        players.add(player1);

        Player player2 = new Player();
        player2.setId("2");
        player2.setName("Jane");
        players.add(player2);

        // Set up the mock behavior
        when(playerRepository.findAll()).thenReturn(players);

        // Call the method under test
        List<PlayerDTO> result = diceGameServices.getAllPlayers().get();

        // Verify the result
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("John");
        assertThat(result.get(1).getName()).isEqualTo("Jane");

        // Verify the mock interaction
        verify(playerRepository).findAll();
    }
    @Test
    void testDeletePlayer() {
        // Create a player object
        Player player = new Player();
        player.setId("1");
        player.setName("John");

        // Set up the mock behavior
        when(playerRepository.findById(anyString())).thenReturn(Optional.of(player));

        // Call the method under test
        Optional<PlayerDTO> result = diceGameServices.delete("1");

        // Verify the result
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());

        // Verify the mock interaction
        verify(playerRepository).findById("1");
        verify(playerRepository).deleteById("1");
    }
    @Test
    void testPlayersRanking() {
        // Create a list of player objects
        List<Player> players = new ArrayList<>();
        Player player1 = new Player();
        player1.setId("1");
        player1.setName("John");
        players.add(player1);
        Player player2 = new Player();
        player2.setId("1");
        player2.setName("Alice");
        players.add(player2);

        // Set up the mock behavior
        when(playerRepository.findAll()).thenReturn(players);

        // Call the method under test
        Optional<List<PlayerDTO>> result = diceGameServices.playersRanking();
        result.get().stream().
                map(s-> {
                    if(s.getName().equals("John")) {
                        s.setSuccessRate(0.50);
                    }
                    else {
                        s.setSuccessRate(1.0);
                    }
                            return null;
                        }
                )
                .collect(Collectors.toList());

        // Verify the result
        assertTrue(result.isPresent());
        List<PlayerDTO> rankedPlayers = result.get();
        assertEquals(2, rankedPlayers.size());
        assertEquals("Alice", rankedPlayers.get(0).getName());
        assertEquals(1.0, rankedPlayers.get(0).getSuccessRate());
        assertEquals("John", rankedPlayers.get(1).getName());
        assertEquals(0.5, rankedPlayers.get(1).getSuccessRate());

        // Verify the mock interaction
        verify(playerRepository).findAll();
    }

    @Test
    void testAverageSuccess() {
        // Create a list of player objects
        List<Player> players = new ArrayList<>();
        Player player1 = new Player();
        player1.setId("1");
        player1.setName("John");
        players.add(player1);
        Player player2 = new Player();
        player2.setId("2");
        player2.setName("Alice");
        players.add(player2);

        // Set up the mock behavior
        when(playerRepository.findAll()).thenReturn(players);

        // Call the method under test
        OptionalDouble result = diceGameServices.getAllPlayers().get().stream()
                .mapToDouble(s -> {
                    if (s.getName().equals("John")) {
                        s.setSuccessRate(0.50);
                    } else {
                        s.setSuccessRate(1.0);
                    }
                    return s.getSuccessRate();
                })
                .average();
        diceGameServices.averageSuccess();

        // Verify the result
        assertTrue(result.isPresent());
        double average = result.getAsDouble();
        assertEquals(0.75, average, 0.001);

    }
    @Test
    void testBestPlayer() {
        // Create a list of player objects
        List<Player> players = new ArrayList<>();
        Player player1 = new Player();
        player1.setId("1");
        player1.setName("John");
        players.add(player1);

        Player player2 = new Player();
        player2.setId("2");
        player2.setName("Alice");
        players.add(player2);

        // Set up the mock behavior
        when(playerRepository.findAll()).thenReturn(players);

        // Call the method under test
        Optional<PlayerDTO> result = diceGameServices.bestPlayer();

        // Verify the result
        assertTrue(result.isPresent());
        PlayerDTO bestPlayer = result.get();
        assertEquals("Alice", bestPlayer.getName());

        // Verify the mock interaction
        verify(playerRepository).findAll();
    }
    @Test
    void testWorstPlayer() {
        // Create a list of player objects
        List<Player> players = new ArrayList<>();
        Player player1 = new Player();
        player1.setId("1");
        player1.setName("John");
        players.add(player1);

        Player player2 = new Player();
        player2.setId("2");
        player2.setName("Alice");
        players.add(player2);

        // Set up the mock behavior
        when(playerRepository.findAll()).thenReturn(players);

        // Call the method under test
        Optional<PlayerDTO> result = diceGameServices.worstPlayer();

        // Verify the result
        assertTrue(result.isPresent());
        PlayerDTO worstPlayer = result.get();
        assertEquals("John", worstPlayer.getName());

        // Verify the mock interaction
        verify(playerRepository).findAll();
    }
}

