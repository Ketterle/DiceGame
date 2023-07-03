package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Game;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.GameStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PlayerWithGamesDTOTest {

    @Test
    public void successRatePlayerCalculatorTest() {
        Game game1 = Mockito.mock(Game.class);
        Game game2 = Mockito.mock(Game.class);
        Game game3 = Mockito.mock(Game.class);

        List<Game> games = new ArrayList<>();
        games.add(game1);
        games.add(game2);
        games.add(game3);

        PlayerWithGamesDTO playerWithGamesDTO = new PlayerWithGamesDTO();
        playerWithGamesDTO.setGames(games);

        when(game1.getGameStatus()).thenReturn(GameStatus.WIN);
        when(game2.getGameStatus()).thenReturn(GameStatus.LOSE);
        when(game3.getGameStatus()).thenReturn(GameStatus.WIN);

        double expectedSuccessRate = (2.0 / 3.0) * 100;

        assertEquals(expectedSuccessRate, playerWithGamesDTO.successRatePlayerCalculator());
    }

    @Test
    public void compareToTest() {
        PlayerWithGamesDTO player1 = new PlayerWithGamesDTO();
        player1.setName("Player 1");
        player1.setSuccessRate(80.0);

        PlayerWithGamesDTO player2 = new PlayerWithGamesDTO();
        player2.setName("Player 2");
        player2.setSuccessRate(70.0);

        PlayerWithGamesDTO player3 = new PlayerWithGamesDTO();
        player3.setName("Player 3");
        player3.setSuccessRate(80.0);

        PlayerWithGamesDTO player4 = new PlayerWithGamesDTO();
        player4.setName("Player 3");
        player4.setSuccessRate(80.0);

        int result1 = player1.compareTo(player2);
        int result2 = player2.compareTo(player1);
        int result3 = player1.compareTo(player3);
        int result4 = player3.compareTo(player4);

        assertEquals(-1, result1);
        assertEquals(1, result2);
        assertEquals(-2, result3);
        assertEquals(0, result4);
    }
}
