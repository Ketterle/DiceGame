package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.GameStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
 /* This class sets how games are retrieved and showed to the user. Gets rid of id */
public final class GameDTO {

    @Getter
    @Setter
    private int dice1;

    @Getter
    @Setter
    private int dice2;

    @Getter
    @Setter
    private int gameOutput;

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private GameStatus gameStatus;
}
