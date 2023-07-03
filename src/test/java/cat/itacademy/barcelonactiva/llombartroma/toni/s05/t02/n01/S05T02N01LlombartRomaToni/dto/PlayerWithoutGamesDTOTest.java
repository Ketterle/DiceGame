package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerWithoutGamesDTOTest {
    @Test
    public void compareToTest() {
        PlayerWithoutGamesDTO player1 = new PlayerWithoutGamesDTO();
        player1.setName("Player 1");
        player1.setSuccessRate(80.0);

        PlayerWithoutGamesDTO player2 = new PlayerWithoutGamesDTO();
        player2.setName("Player 2");
        player2.setSuccessRate(70.0);

        PlayerWithoutGamesDTO player3 = new PlayerWithoutGamesDTO();
        player3.setName("Player 3");
        player3.setSuccessRate(80.0);

        PlayerWithoutGamesDTO player4 = new PlayerWithoutGamesDTO();
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
