package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerRankingDTOTest {
    @Test
    public void testCompareTo() {
        PlayerRankingDTO player1 = new PlayerRankingDTO();
        player1.setName("Player 1");
        player1.setSuccessRate(80.0);

        PlayerRankingDTO player2 = new PlayerRankingDTO();
        player2.setName("Player 2");
        player2.setSuccessRate(70.0);

        PlayerRankingDTO player3 = new PlayerRankingDTO();
        player3.setName("Player 3");
        player3.setSuccessRate(80.0);

        PlayerRankingDTO player4 = new PlayerRankingDTO();
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
