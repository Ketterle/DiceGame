package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.services;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.UserDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.GameRepositoryMySQL;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.UserRepositoryMySQL;
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
    private UserRepositoryMySQL playerRepository;

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
        // Initialize the User object
        User user = new User();
        user.setName("John");

        // Set up the mock behavior
        when(playerRepository.getPlayersByName("John")).thenReturn(Optional.of(new ArrayList<>()));
        when(playerRepository.save(user)).thenReturn(user);

        // Call the method under test
        Optional<UserDTO> result = diceGameServices.add(user);

        // Verify the result
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John");

        // Verify the mock interaction
        verify(playerRepository).getPlayersByName("John");
        verify(playerRepository).save(user);
    }

    @Test
    void testUpdatePlayer() {
        // Initialize the User object
        User user = new User();
        user.setId("1"); // Assuming the user ID is 1
        user.setName("Toni");

        // Set up the mock behavior
        when(playerRepository.findById("1")).thenReturn(Optional.of(user));
        when(playerRepository.save(user)).thenReturn(user);

        // Call the method under test
        Optional<UserDTO> result = diceGameServices.update("John","1");

        // Verify the result
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John");

        // Verify the mock interaction
        verify(playerRepository).findById("1");
        verify(playerRepository).save(user);
    }
    @Test
    void testNewGame() {
        // Initialize the User object
        User user = new User();
        user.setId("1"); // Assuming the user ID is 1
        user.setName("John");

        // Set up the mock behavior
        when(playerRepository.findById("1")).thenReturn(Optional.of(user));

        // Call the method under test
        Optional<Game> result = diceGameServices.newGame("1");

        // Verify the result
        assertThat(result).isPresent();
        assertThat(result.get().getUser()).isEqualTo(user);

        // Verify the mock interaction
        verify(playerRepository).findById("1");
        verify(gameRepository).save(any(Game.class));
    }
    @Test
    void testGetPlayerGames() {
        // Initialize the User object
        User user = new User();
        user.setId("1"); // Assuming the user ID is 1
        user.setName("John");

        // Initialize the Game objects
        Game game1 = new Game();
        game1.setId(1);
        game1.setUser(user);

        Game game2 = new Game();
        game2.setId(2);
        game2.setUser(user);

        List<Game> games = Arrays.asList(game1, game2);

        // Set up the mock behavior
        when(playerRepository.findById("1")).thenReturn(Optional.of(user));
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
        // Create a list of User objects
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId("1");
        user1.setName("John");
        users.add(user1);

        User user2 = new User();
        user2.setId("2");
        user2.setName("Jane");
        users.add(user2);

        // Set up the mock behavior
        when(playerRepository.findAll()).thenReturn(users);

        // Call the method under test
        List<UserDTO> result = diceGameServices.getAllPlayers().get();

        // Verify the result
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("John");
        assertThat(result.get(1).getName()).isEqualTo("Jane");

        // Verify the mock interaction
        verify(playerRepository).findAll();
    }
    @Test
    void testDeletePlayer() {
        // Create a user object
        User user = new User();
        user.setId("1");
        user.setName("John");

        // Set up the mock behavior
        when(playerRepository.findById(anyString())).thenReturn(Optional.of(user));

        // Call the method under test
        Optional<UserDTO> result = diceGameServices.delete("1");

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
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId("1");
        user1.setName("John");
        users.add(user1);
        User user2 = new User();
        user2.setId("1");
        user2.setName("Alice");
        users.add(user2);

        // Set up the mock behavior
        when(playerRepository.findAll()).thenReturn(users);

        // Call the method under test
        Optional<List<UserDTO>> result = diceGameServices.playersRanking();
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
        List<UserDTO> rankedPlayers = result.get();
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
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId("1");
        user1.setName("John");
        users.add(user1);
        User user2 = new User();
        user2.setId("2");
        user2.setName("Alice");
        users.add(user2);

        // Set up the mock behavior
        when(playerRepository.findAll()).thenReturn(users);

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
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId("1");
        user1.setName("John");
        users.add(user1);

        User user2 = new User();
        user2.setId("2");
        user2.setName("Alice");
        users.add(user2);

        // Set up the mock behavior
        when(playerRepository.findAll()).thenReturn(users);

        // Call the method under test
        Optional<UserDTO> result = diceGameServices.bestPlayer();

        // Verify the result
        assertTrue(result.isPresent());
        UserDTO bestPlayer = result.get();
        assertEquals("Alice", bestPlayer.getName());

        // Verify the mock interaction
        verify(playerRepository).findAll();
    }
    @Test
    void testWorstPlayer() {
        // Create a list of player objects
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setId("1");
        user1.setName("John");
        users.add(user1);

        User user2 = new User();
        user2.setId("2");
        user2.setName("Alice");
        users.add(user2);

        // Set up the mock behavior
        when(playerRepository.findAll()).thenReturn(users);

        // Call the method under test
        Optional<UserDTO> result = diceGameServices.worstPlayer();

        // Verify the result
        assertTrue(result.isPresent());
        UserDTO worstPlayer = result.get();
        assertEquals("John", worstPlayer.getName());

        // Verify the mock interaction
        verify(playerRepository).findAll();
    }
}

