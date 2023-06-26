package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.modelmapper.ModelMapper;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
public final class Game {
    {
        this.dice1 = diceThrown();
        this.dice2 = diceThrown();
        this.gameOutput = this.dice1 + this.dice2;
        this.gameStatus = gameStatusCalculator(this.gameOutput);
    }
    private static final int MAX_DICE_VALUE = 6;
    private static final int GAME_WINNING_OUTPUT = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int dice1;

    @Column
    private int dice2;

    @Column
    private int gameOutput;

    @Enumerated(EnumType.STRING)
    @Column
    private GameStatus gameStatus;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL , optional = false)
    @JoinColumn(name = "id_player", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User player;

    private static int diceThrown() {
        return (int) (Math.random() * MAX_DICE_VALUE) + 1;
    }

    private static GameStatus gameStatusCalculator(int gameOutput) {
        return gameOutput == GAME_WINNING_OUTPUT ? GameStatus.WIN : GameStatus.LOSE;
    }
    public static GameDTO fromGameToGameDTO(Game game) {
        ModelMapper modelMapper = new ModelMapper();
        GameDTO gameDTO = modelMapper.map(game, GameDTO.class);
        return gameDTO;
    }
}
