package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.GameStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameDTOTest {
    @Test
    public void GameDTOTest() {
        int dice1 = 3;
        int dice2 = 4;
        int gameOutput = dice1 + dice2;
        GameStatus gameStatus = GameStatus.WIN;

        GameDTO gameDTO = new GameDTO();
        gameDTO.setDice1(dice1);
        gameDTO.setDice2(dice2);
        gameDTO.setGameOutput(gameOutput);
        gameDTO.setGameStatus(gameStatus);

        assertEquals(dice1, gameDTO.getDice1());
        assertEquals(dice2, gameDTO.getDice2());
        assertEquals(gameOutput, gameDTO.getGameOutput());
        assertEquals(gameStatus, gameDTO.getGameStatus());
    }
}
