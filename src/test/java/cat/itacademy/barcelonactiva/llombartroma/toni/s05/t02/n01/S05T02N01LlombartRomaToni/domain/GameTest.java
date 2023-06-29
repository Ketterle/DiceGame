package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameTest {

    @Mock
    private User player;

    @InjectMocks
    private Game game;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        game = new Game();
        game.setPlayer(player);
    }

    @Test
    public void GameTest() {
        int dice1 = 2;
        int dice2 = 5;
        game.setGameOutput(7);
        game.setGameStatus(GameStatus.WIN);
        GameStatus gameStatus = game.getGameStatus();

        assertEquals(dice1 + dice2, game.getGameOutput());
        assertEquals(GameStatus.WIN, gameStatus);
    }

    @Test
    public void fromGameToGameDTOTest() {
        ModelMapper modelMapper = new ModelMapper();

        GameDTO gameDTO = Game.fromGameToGameDTO(game);

        assertEquals(game.getDice1(), gameDTO.getDice1());
        assertEquals(game.getDice2(), gameDTO.getDice2());
        assertEquals(game.getGameOutput(), gameDTO.getGameOutput());
        assertEquals(game.getGameStatus(), gameDTO.getGameStatus());
    }
}

