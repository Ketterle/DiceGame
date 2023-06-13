package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.GameDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.dto.PlayerDTO;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repository.GameRepositoryMySQL;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repository.PlayerRepositoryMySQL;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.modelmapper.ModelMapper;

@Entity
@Table(name = "games")
public class Game {
    private static final int MAX_DICE_VALUE = 6;
    private static final int GAME_WINNING_OUTPUT = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;

    @Column
    @Getter
    @Setter
    private int dice1;

    @Column
    @Getter
    @Setter
    private int dice2;

    @Column
    @Getter
    @Setter
    private int gameOutput;

    @Enumerated(EnumType.STRING)
    @Column
    @Getter
    @Setter
    private GameStatus gameStatus;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL , optional = false)
    @JoinColumn(name = "id_player", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @Getter
    @Setter
    private Player player;

    public Game() {
        this.dice1 = diceThrown();
        this.dice2 = diceThrown();
        this.gameOutput = dice1 + dice2;
        this.gameStatus = gameStatusCalculator(gameOutput);
    }

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
